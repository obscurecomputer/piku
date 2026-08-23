
--local menu = game.ui.menu()
--
--menu.row()
--	.anchor("center_center")
--	.addBox()
--	.width("50%")
--	.height("50%")
--	.background(color.rgb(255,255,255))
--
--menu.open()
--
listen("client.key_update", function(event)
	log.info(event.key)
	--if event.key == "y" and event.action == "press" then
	--	menu.open()
	--end
end)