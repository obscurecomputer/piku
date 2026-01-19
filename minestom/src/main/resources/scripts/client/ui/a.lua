
local ui = game.ui
local looptimes = 0

listen("client.key_update", function(event)
    log.info("hi")
    if event.key == "y" and event.action == "press" then
        local group = ui.group("line_test")

--         local line = group.line("test_line")
--             .to(vec2.of(50, 20))
--             .from(vec2.of(0, 0))
--             .color(color.rgb(160, 160, 160))
--             .pointSize(vec2.of(5, 5))
--
--         line.animate()
--             .to(vec2.of(100, -50), 5, "ease_in_out_cubic")
--             .play()

        looptimes = 0
        scheduler
            .task(function(task)
                looptimes = looptimes + 1
                if looptimes > 30 then
                    task.cancel()
                    return
                end
--                 local text = group.text("text")
--                     .text("HI")
--
--                 text.animate()
--                     .move(vec2.of(100, -50), 5, "ease_in_out_cubic")
--                     .play()



                    local line = group.line("test_line")
                        .to(vec2.of(50, 20))
                        .from(vec2.of(0, 0))
                        .color(color.rgb(160, 160, 160))
                        .pointSize(vec2.of(20, 20))

                    line.animate()
                        .to(vec2.of(100, -50), 5, "ease_in_out_cubic")
                        .play()
            end)
            .loop(10)
            .run()
    end
    if event.key == "i" and event.action == "press" then
        ui.get("line_test").remove()
    end
end)