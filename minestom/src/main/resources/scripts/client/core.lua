
local ui = game.ui

local group = ui.group("test")

local sprite = group.sprite("test_sprite")
sprite.texture = "textures/block/dirt.png"
sprite.size = vec2.of(64, 32)

send("test.bro", {})

listen("client.key_update", function(event)
--     print(event)
--     if event.key == "y" and event.action == "press" then
--         client.send("<yellow>Began holding Y!")
--     end
--     if event.key == "y" and event.action == "release" then
--         client.send("<yellow>Released Y, updating UI!")
--         local sprite = ui.get("test").get("test_sprite")
--         sprite.size = vec2.of(64, 64)
--     end
end)