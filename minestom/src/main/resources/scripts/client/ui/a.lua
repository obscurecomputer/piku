
local ui = game.ui

listen("client.key_update", function(event)
    if event.key == "y" and event.action == "press" then
        local group = ui.group("line_test")

        local line = group.line("test_line")
            .to(vec2.of(50, 20))
            .from(vec2.of(0, 0))
            .color(color.rgb(160, 160, 160))
            .pointSize(vec2.of(5, 5))

        line.animate()
            .to(vec2.of(100, -50), 5, "ease_in_out_cubic")
            .play()

    end
    if event.key == "i" and event.action == "press" then
        ui.get("line_test").remove()
    end
end)