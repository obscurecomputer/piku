
events.listen("server.player_join", function(event)
	event.player.send(event.player)
	event.player.send("HI")
	net.emit("test", event.player, event.player)
end)