local game_showcase_comps = {}
local current_index = 1

local max_visible = 5

local normal_size = vec2.of(125, 125)
local game_art_normal_size = normal_size
local normal_color = color.rgba(0, 0, 0, 0)

local selected_color = color.hsl(200, 100, 50)
local selected_size = vec2.of(normal_size.x * 1.25, normal_size.y * 1.25)

-- The visible slot position where the selected item should remain while scrolling.
-- Counts from the first visible item.
--
-- Example:
-- max_visible = 5
-- scroll_position = 3
--
-- The selected item will stay in the middle (3rd slot)
-- while the row scrolls around it.
local scroll_position = 3

-- The initial delay (in ticks) before the repeat delay should kick in
local initial_delay = 10

-- The repeat delay (in ticks) between each movement in the held direction
local repeat_delay = 4

-- Whether the controller has given enough input within the engage threshold
-- to be held
local holding = false

-- Number of ticks elapsed while the current direction has been held on the controller.
local ticks = 0

-- Current movement direction directly from the controller
-- -1: left
--  0: no input
--  1: right
local direction = 0

-- The LOCKED movement direction used for scrolling
local held_direction = 0

-- Whether the input has reached the repeating phase.
-- false: waiting for the initial_delay
-- true: selection will continue moving at the repeat_delay
local repeating = false

-- Stick hysteresis thresholds:
-- engage_threshold:
-- 	The minimum analogue value required to begin scrolling in a direction
-- 	Prevents accidental input from controller drift, bounce back, or small movements
-- release_threshold:
-- 	The stick must return below this value before releasing the input
-- 	Prevents the controller spring from bouncing in the opposite direction, counting as
-- 	an input
local engage_threshold = 0.25
local release_threshold = 0.10

-- The interval to check for controller input (in ticks)
local scheduler_interval = 1

local game_showcase = game.ui.row()
	.anchor("center_center")
	.mainAxis("start")
	.crossAxis("center")
	.gap(10)
	.scrollable(true)
	.width((max_visible * normal_size.x + selected_size.x) - (max_visible * 10))
	.padding(spacing.of(30))

local textures = {
	"block/dirt",
	"block/stone",
	"block/gold_block",
	"block/iron_block",
	"block/diamond_block",
	"block/emerald_block",
	"block/redstone_block",
	"block/coal_block",
	"block/copper_block",
	"block/deepslate",
	"block/grass_block_side",
	"block/oak_planks",
	"block/spruce_planks",
	"block/birch_planks",
	"block/jungle_planks",
	"block/acacia_planks",
	"block/dark_oak_planks",
	"block/bricks",
	"block/glass",
	"block/obsidian",
	"block/crying_obsidian",
	"block/netherite_block",
	"block/amethyst_block",
	"block/quartz_block",
	"block/prismarine",
	"block/sea_lantern",
	"block/ice",
	"block/packed_ice",
	"block/snow",
	"block/moss_block"
}

for i = 1, 20 do
	local texture = textures[((i - 1) % #textures) + 1]

	local game = game_showcase.addBox()
	.background(normal_color)
	.size(normal_size)

	game.addSprite()
	.texture("minecraft:textures/" .. texture .. ".png")
	.anchor("center_center")
	.size(game_art_normal_size)

	table.insert(game_showcase_comps, game)
end

local function update_selection()
	local item_spacing = 10
	local items = current_index * normal_size.x

	local new_scroll = (current_index * normal_size.x) + (item_spacing * current_index) - (normal_size.x * scroll_position)
	game_showcase.animate()
		.scroll(new_scroll, 0.1, "ease_in_out")
		.play()

	for i, box in ipairs(game_showcase_comps) do
		if i == current_index then
			box.animate()
			.background(selected_color, 0.15, "ease_in_out")
			.size(selected_size, 0.15, "ease_in_out")
			.play()
		else
			box.animate()
				.background(normal_color, 0.15, "ease_in_out")
				.size(normal_size, 0.15, "ease_in_out")
				.play()
		end
	end
end

scheduler.task(function(task)
	if not holding then return end

	ticks += scheduler_interval

	if not repeating then
		-- initial movement
		if ticks == scheduler_interval then
			find_next(direction)
		end

		-- start repeat phase
		if ticks >= initial_delay then
			repeating = true
			ticks = 0
		end
	else
		-- repeat movement
		if ticks >= repeat_delay then
			ticks = 0
			find_next(direction)
		end
	end
end)
	.loop(scheduler_interval)
	.run()

listen("client.controller.move", function(event)
	local x = event.vector.x

	-- No direction is currently held
	-- Require the engage threshold before accepting input
	if held_direction == 0 then
		-- Stick pushed right
		if x > engage_threshold then
			direction = 1
			holding = true
		-- Stick pushed left
		elseif x < -engage_threshold then
			direction = -1
			holding = true
		end
	-- A direction is already active, don't immediately
	-- change from small stick movements
	else
		-- Allow deliberate direction change through the engage threshold
		if held_direction == 1 and x < -engage_threshold then
			direction = -1
		elseif held_direction == -1 and x > engage_threshold then
			direction = 1
		-- Release the input only once the x is within the release threshold
		elseif math.abs(x) < release_threshold then
			direction = 0
			holding = false
			repeating = false
			ticks = 0
		end
	end

	-- Update the currently locked direction
	-- preventing small movements from causing
	-- accidental direction changes
	if direction ~= 0 and held_direction ~= direction then
		held_direction = direction
	end

	-- Reset when the stick stops receiving input
	if direction == 0 then
		held_direction = 0
	end
end)

function find_next(direction)
	current_index += direction

	if current_index < 1 then
		current_index = #game_showcase_comps
	elseif current_index > #game_showcase_comps then
		current_index = 1
	end

	update_selection()
end

update_selection()