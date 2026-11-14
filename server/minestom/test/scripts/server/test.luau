
events.listen("server.player_join", function(event)
	log.info(event.player)
	net.emit("test", "hello", event.player)
end)
net.listen("player.click", function(data, player)
	log.info(data)
	player.send(data)
end)