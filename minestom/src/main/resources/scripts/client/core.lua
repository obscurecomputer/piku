
local ui = game.ui

-- local sprite = group.sprite("test_sprite")
-- sprite.texture = "textures/block/dirt.png"
-- sprite.size = vec2.of(32, 32)

function render()
    local group = ui.group("test")

    group.sprite("test_sprite")
        .texture("legacylands:sprites/world.png")
        .size(vec2.of(64, 64))

    group.text("test_text")
        .text("hi")
end

-- local grad = group.gradient("test_gradient")
-- grad.from = color.rgb(100, 100, 100)
-- grad.to = color.rgb(255, 255, 100)
-- grad.size = vec2.of(100, 50)
--
-- easing.new("hello", function(t)
--     return math.min(t / 0.5, 1)
-- end)
--
-- grad.move(
--     vec2.of(50, 50),
--     3,
--     "hello"
-- )

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        client.send("<yellow>Began holding Y!")
        render()
    end
    if event.key == "y" and event.action == "release" then
        client.send("<yellow>Released Y!")
        ui.get("test").remove()
    end
end)