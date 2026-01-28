
listen("client.key_update", function(event)
    if event.key == "m" and event.action == "press" then

        local flow = game.ui.flow("fucking_kys")
            .pos(vec2.of(50, 0))
            .direction("h")
            .gap(15)

        local testflow1 = flow.flow("flow6")
            .direction("v")
            .gap(5)
        testflow1.text("text1").text("above line")
        testflow1.text("text2").text("below line")

        local testflow2 = flow.flow("flow")
            .direction("v")
            .gap(5)
        testflow2.text("text1").text("above line")
        testflow2.text("text2").text("below line")
    end
    if event.key == "," and event.action == "press" then
        game.ui.debug(true)
    end
    if event.key == "." and event.action == "press" then
        game.ui.debug(false)
    end
end)