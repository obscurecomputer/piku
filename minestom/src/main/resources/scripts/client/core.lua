
local ui = game.ui

easing.new("hello", function(t)
    return math.min(t / 0.5, 1)
end)

function render()
    local group = ui.group("test")

    local sprite = group.sprite("test_sprite")
        .texture("legacylands:sprites/world.png")
        .size(vec2.of(0, 0))
        .pos(vec2.of(0, -200))
        .opacity(0)
        .anchor("center_center")

    sprite.animate()
        .opacity(1, 0.5, "linear")
        .move(vec2.of(0, 0), 0.5, "ease_out_bounce")
        .size(vec2.of(64, 64), 0.5, "ease_out_bounce")
        .play()

    local text = group.text("test_text")
        .text("LOADING...")
        .scale(vec2.of(1, 1))
        .color(color.rgb(0, 0, 0))
        .backgroundColor(color.rgb(255, 255, 255))
        .padding(spacing.of(20, 20, 20, 20))
        .bottomOf(sprite.id)

    text.animate()
        .scale(vec2.of(3, 3), 5, "ease_out_bounce")
        .play()
end

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        client.send("<yellow>Began holding Y!")
        render()
    end
    if event.key == "]" and event.action == "press" then
        ui.get("test").remove()
    end
end)