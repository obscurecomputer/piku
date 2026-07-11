

local game_showcase = game.ui.row()
	.anchor("center_center")
	.mainAxis("start")
	.crossAxis("center")
	.gap(10)

game_showcase.controller()
	.active(true)
	.scrollAxis("h")
	--.allowMultiActivate(true)
	--.toggleActivate(true)
	.allowSingleToggleActivate(true)


local textures = {
	"block/glass",
	"block/white_stained_glass"
}

local selected_color = vec2.of(32, 32)
local normal_color = vec2.of(16, 16)

for i = 1, 14 do
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