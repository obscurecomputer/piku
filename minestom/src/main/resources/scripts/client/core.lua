
local ui = game.ui

function render()
    local group = ui.group("loading_screen")

    local box = group.box("background")
        .color(color.rgb(0, 0, 0))
        .fillScreen()
        .opacity(0)
        .anchor("top_left")

    local sprite = group.sprite("test_sprite")
        .texture("legacylands:sprites/world.png")
        .size(vec2.of(64, 64))
        .pos(vec2.of(0, -200))
        .opacity(0)
        .anchor("center_center")

    local text = group.text("test_text")
        .text("LOADING")
        .scale(vec2.of(2, 2))
        .color(color.rgb(0, 0, 0))
        .backgroundColor(color.rgb(255, 255, 255))
        .padding(spacing.of(2,2,2,2))
        .pos(vec2.of(3, 6))
        .bottomOf(sprite.id)

    local progressBar = group.progressBar("test_bar")
        .progress(0)
        .emptyColor(color.rgb(190, 190, 190))
        .fillColor(color.rgb(255, 255, 255))
        .pos(vec2.of(0, -10))
        .size(vec2.of(300, 10))
        .anchor("bottom_center")
        .opacity(0)

    box.animate()
        .opacity(1, 1, "EASE_IN_OUT_CUBIC")
        .play()
    sprite.animate()
        .opacity(1, 2, "EASE_IN_OUT_CUBIC")
        .move(vec2.of(0, 0), 2, "EASE_IN_OUT_CUBIC")
        .play()
    text.animate()
        .scale(vec2.of(3, 3), 5, "EASE_IN_OUT_CUBIC")
        .play()
    progressBar.animate()
        .opacity(1, 2, "EASE_IN_OUT_CUBIC")
        .progress(1, 5, "EASE_IN_OUT_CUBIC")
        .play()
end

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        client.send("<yellow>Began holding Y!")
        render()
    end
    if event.key == "]" and event.action == "press" then
        ui.get("loading_screen").remove()
    end
    if event.key == "[" and event.action == "press" then
        local screen = ui.get("loading_screen")

        screen.get("background").animate()
            .opacity(0, 2, "EASE_IN_OUT_CUBIC")
            .play()
        screen.get("test_sprite").animate()
            .opacity(0, 2, "EASE_IN_OUT_CUBIC")
            .play()
        screen.get("test_text").animate()
            .opacity(0, 2, "EASE_IN_OUT_CUBIC")
            .play()
        screen.get("test_bar").animate()
            .opacity(0, 2, "EASE_IN_OUT_CUBIC")
            .play()
    end
end)