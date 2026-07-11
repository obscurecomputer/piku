local col = game.ui.row()
	.anchor("center_center")
	.mainAxis("start")
	.crossAxis("center")
col.controller()
	.active(true)
	.scrollAxis("h")
	.edgeMode("r")
local game_showcase = col.addColumn()
.anchor("center_center")
.mainAxis("start")
.crossAxis("center")
.gap(10)

local game_showcase2 = col.addColumn()
.anchor("center_center")
.mainAxis("start")
.crossAxis("center")
.gap(10)
.offset(vec2.of(50, 0))

game_showcase.onFocus(function(this)
	game_showcase.animate()
	.background(color.hsl(200, 100, 50), 0.5, "linear")
	.play()
end)
game_showcase.onUnfocus(function(this)
	game_showcase.animate()
	.background(color.hsl(200, 100, 10), 0.5, "linear")
	.play()
end)

game_showcase2.onFocus(function(this)
	game_showcase2.animate()
	.background(color.hsl(200, 100, 50), 0.5, "linear")
	.play()
end)
game_showcase2.onUnfocus(function(this)
	game_showcase2.animate()
	.background(color.hsl(200, 100, 10), 0.5, "linear")
	.play()
end)

game_showcase.controller()
.active(true)
.scrollAxis("v")
--.allowMultiActivate(true)
--.toggleActivate(true)
.allowSingleToggleActivate(true)

game_showcase2.controller()
.active(true)
.scrollAxis("v")
--.allowMultiActivate(true)
--.toggleActivate(true)
.allowSingleToggleActivate(true)

function new(container)
	local textures = {
		"block/glass",
		"block/white_stained_glass"
	}

	local selected_color = vec2.of(32, 32)
	local normal_color = vec2.of(16, 16)

	for i = 1, 14 do
		local texture = textures[((i - 1) % #textures) + 1]

		local game = container.addBox()

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
			game.animate()
			.size(selected_color, 0.5, "ease_in_out")
			.background(color.random(), 0.5, "ease_in_out")
			.play()
		end)
		game.onDeactivate(function(this)
			game.animate()
			.size(normal_color, 0.5, "ease_in_out")
			.background(color.random(), 0.5, "ease_in_out")
			.play()
		end)

		if i == 10 then
			game.selectable(false)
		end
	end
end


new(game_showcase)
new(game_showcase2)