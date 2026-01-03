
local ui = game.ui

local group = ui.group("test")

local sprite = group.sprite("test_sprite")
sprite.texture = "textures/block/dirt.png"
sprite.size = vec2.of(32, 32)

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        client.send("<yellow>Began holding Y!")
    end
    if event.key == "y" and event.action == "release" then
        client.send("<yellow>Released Y, updating UI!")
        local gotsprite = ui.get("test").get("test_sprite")
        gotsprite.size = vec2.of(gotsprite.size.x + 1, gotsprite.size.y + 1)
    end
    if event.key == "b" and event.action == "press" then
        client.send("<yellow>Began holding b!")
        send("test.hi", 5)
    end
end)