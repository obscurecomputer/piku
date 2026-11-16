
net.listen("test", function(player)
	client.send(player.position)
	client.send(player.position.pitch)
	player.send("HELLO")
end)