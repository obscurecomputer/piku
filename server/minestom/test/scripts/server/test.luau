
print("hi")

--local inst = instance.new()
--
--inst.generator(function(unit)
--	local start = unit.absoluteStart
--	local size = unit.size
--
--	for x = 0, size.x - 1 do
--		for z = 0, size.z - 1 do
--			local pos = start.add(x, 5, z)
--			unit.set(pos, blocks.get("dirt"))
--		end
--	end
--end)

events.listen("server.player_join", function(event)
	event.player.gameMode("creative")
	--event.player.instance(inst)

	local start = blockvec.new(0,0,0)

	print("HELLO")
	local t = os.clock()
	for i = 1, 20000 do start.add(1, 0, 0) end
	event.player.send("add " .. tostring(os.clock() - t))

	t = os.clock()
	for i = 1, 20000 do blocks.get("dirt") end
	event.player.send("blocks.get " .. tostring(os.clock() - t))
end)