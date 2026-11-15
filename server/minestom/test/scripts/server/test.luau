
events.listen("server.player_join", function(event)
	net.emit("test", event.player, event.player)
end)