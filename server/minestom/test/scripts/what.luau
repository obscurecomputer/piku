
local menu = game.ui.menu()
	.blur(true)
	.escapeClose(true)

local div = menu.column()
	.anchor("center_center")
	.gap(10)
local box = div
	.addBox()

local red = color.rgb(255, 0, 0)
local white = color.rgb(255,255,255)
box.width("50%")
	.height("50%")
	.background(white)
	.onPress(function(event)
		log.info(event)
		log.info(event)
		box.background(red)
	end)
	.onRelease(function(event)
		box.background(white)
	end)

div.addDivider()
	.thickness(15)
	.width("50%")

menu.open()

listen("client.key_update", function(event)
	if event.key == "y" and event.action == "press" then
		menu.open()
	end
	if event.key == "[" and event.action == "press" then
		box.hitShape("r")
	end
	if event.key == "]" and event.action == "press" then
		box.hitShape("e")
	end
end)