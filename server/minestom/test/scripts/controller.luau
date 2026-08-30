
local test_box = game.ui.box()
    .background(color.rgba(255,255,255,50))
	.anchor("center_center")
    .width(200)
    .height(200)

local controls = test_box.addColumn()
	.offset(vec2.of(0, -18))
	.gap(2)
	.background(color.rgba(255,255,255,50))

controls.addText()
	.text(
		text.literal("LOOK VECTOR")
			.append(controlify.glyph("move_left"))
			.color(color.rgb(255, 255, 0))
			.bold()
	)
controls.addText()
	.text(
		text.literal("MOVE VECTOR")
			.append(controlify.glyph("look_up"))
			.color(color.rgb(255, 0, 0))
			.bold()
	)

local look_line = test_box.addLine()
	.anchor("center_center")
	.offset(vec2.of(0, 0))
	.color(color.rgb(255, 255, 0))
	.fixedOrigin(true)
local move_line = test_box.addLine()
	.anchor("center_center")
	.offset(vec2.of(0, 0))
	.color(color.rgb(255, 0, 0))
	.fixedOrigin(true)

local look_to = vec2.of(0, 0)
local move_to = vec2.of(0, 0)

listen("client.controller.look", function(event)
	look_to.x = event.vector.x * 100
	look_to.y = event.vector.y * -100

	look_line.animate()
		.to(look_to, 0.05, "EASE_IN_OUT")
		.play()
end)

listen("client.controller.move", function(event)
	move_to.x = event.vector.x * 100
	move_to.y = event.vector.y * -100

	move_line.animate()
		.to(move_to, 0.05, "EASE_IN_OUT")
		.play()
end)