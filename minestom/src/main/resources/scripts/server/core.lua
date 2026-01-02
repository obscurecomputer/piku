
print("HELLO FROM MINESTOM!!")

listen("server.player_loaded", function(event)
    print("Hi")
--     print("PLAYER " .. event.player.username .. " JOINED")
--     event.player.send("<red>LOL!")

--     event.player.sendKeybinds({
--         ["key.advancements"] = "key.keyboard.y"
--     })
end)
--
-- listen("test.bro", function(event)
--     print("HI!!!! NO WAY")
-- end)