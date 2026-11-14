
net.listen("test", function(event)
	client.send("hello")
end)
events.listen("client.mouse_update", function(event)
	client.send("hello")
	net.emit("player.click", "wow")
end)