
local ui = game.ui

function render()
    local group = ui.group("loading_screen")

    local assetCount = 0
    local totalAssets = 600

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

    local assetCounter = group.text("asset_counter")
        .text(assetCount .. "/" .. totalAssets)
        .anchor("bottom_center")
        .pos(vec2.of(0, 10))
        .opacity(0)

    scheduler
        .task(function(task)
            if assetCount >= totalAssets then
                task.cancel()
                return
            end
            assetCount = clamp(assetCount + 9, 0, totalAssets)
            ui.get("loading_screen").get("asset_counter")
                .text(assetCount .. "/" .. totalAssets)
        end)
            .loop(1)
            .run()

    box.animate()
        .opacity(1, 1, "EASE_IN_OUT_CUBIC")
        .play()
    sprite.animate()
        .opacity(1, 2, "EASE_IN_OUT_CUBIC")
        .move(vec2.of(0, 0), 2, "EASE_IN_OUT_CUBIC")
        .play()
    progressBar.animate()
        .opacity(1, 2, "EASE_IN_OUT_CUBIC")
        .progress(1, 5, "EASE_IN_OUT_EXPO")
        .play()
    assetCounter.animate()
        .opacity(1, 2, "EASE_IN_OUT_CUBIC")
        .move(vec2.of(0, -4), 2, "EASE_IN_OUT_CUBIC")
        .play()

    scheduler
        .task(function(task)
            local screen = ui.get("loading_screen")

            local fadeOutTime = 3
            local easing = "EASE_OUT_BACK"
            screen.get("background").animate()
                .opacity(0, fadeOutTime, easing)
                .play()
            screen.get("test_sprite").animate()
                .opacity(0, fadeOutTime, easing)
                .move(vec2.of(0, -50), fadeOutTime, easing)
                .play()
            screen.get("test_text").animate()
                .scale(vec2.of(3, 3), fadeOutTime, easing)
                .opacity(0, fadeOutTime, easing)
                .play()
            screen.get("test_bar").animate()
                .opacity(0, fadeOutTime, easing)
                .move(vec2.of(0, 5), fadeOutTime, easing)
                .play()
            screen.get("asset_counter").animate()
                .opacity(0, fadeOutTime, easing)
                .move(vec2.of(0, 10), fadeOutTime, easing)
                .play()

            scheduler
                .task(function(task)
                    ui.get("loading_screen").remove()
                end)
                .delay(40)
                .run()
                end)
        .delay(70)
        .run()
end

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        client.send("<yellow>Began holding Y!")
        render()
    end
    if event.key == "]" and event.action == "press" then
        ui.get("loading_screen").remove()
    end
end)