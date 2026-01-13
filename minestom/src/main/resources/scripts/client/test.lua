
listen("client.key_update", function(event)
    if event.key == "i" and event.action == "press" then
        cancelled = true
    end
    if event.key == "b" and event.action == "press" then
        client.send("<yellow>Began holding B! (test)")

--         client.hideArm = not client.hideArm
--         client.hideHotbar = not client.hideHotbar
        client.hideHUD = not client.hideHUD
        game.camera.toggle()
        game.camera.move(client.headPos)
        game.camera.rotate(client.headRot)

        game.camera.animate()
            .move(
                client.pos.add(
                    vec3.of(0, 50, -20)
                ), 5, "EASE_OUT_BACK"
            )
            .rotate(vec3.of(90, 0, 0), 5, "EASE_OUT_BACK")
            .play()
    end
end)