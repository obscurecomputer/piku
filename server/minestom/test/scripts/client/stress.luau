
listen("client.ui_render", function(event)
	local p = {}
	for i = 1, 1000 do
		p[i] = color.hsl(1, 1, 1)

		client.send(p[i])
	end
end)