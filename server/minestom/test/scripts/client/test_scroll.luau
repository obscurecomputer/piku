

local game_showcase = game.ui.column()
	.anchor("center_center")
	.mainAxis("start")
	.crossAxis("center")
	.gap(10)
	.scrollable(true)

game_showcase.controller()
	.active(true)
	.scrollAxis("h")

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
}

local selected_color = vec2.of(32, 32)
local normal_color = vec2.of(16, 16)

for i = 1, 20 do
	local texture = textures[((i - 1) % #textures) + 1]

	local game = game_showcase.addBox()

	local sprite = game.addSprite()
		.texture("minecraft:textures/" .. texture .. ".png")
		.anchor("center_center")

	game.onSelect(function(this)
		sprite.animate()
			.size(selected_color, 0.15, "ease_in_out")
			.play()
	end)
	game.onDeselect(function(this)
		sprite.animate()
			.size(normal_color, 0.15, "ease_in_out")
			.play()
	end)
	game.onActivate(function(this)
		sprite.animate()
			.size(vec2.of(20, 20), 0.5, "ease_in_out")
			.play()
	end)
end