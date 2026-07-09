
local test_box = game.ui.box()
    .background(color.rgba(255,255,255,50))
	.anchor("center_center")
    .width(200)
    .height(200)

local look_line = test_box.addLine()
	.anchor("center_center")
	.offset(vec2.of(0, 0))
	.color(color.rgb(255, 255, 0))
	.fixedOrigin(true)

local look_to = vec2.of(0, 0)

listen("client.controller.look", function(event)
	client.send("vec: " .. event.vector.tostring())

	look_to.x = event.vector.x * 100
	look_to.y = event.vector.y * -100

	look_line.animate()
		.to(look_to, 0.05, "EASE_IN_OUT")
		.play()
end)