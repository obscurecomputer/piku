
listen("client.key_update", function(event)
    if event.key == "b" and event.action == "press" then
        client.send("<yellow>Began holding B! (test)")

        game.camera.toggle()
        game.camera.move(client.headPos)

        game.camera.animate()
            .move(
                client.pos.add(
                    vec3.of(0, 5, -5)
                ), 10, "linear"
            )
            .play()
    end
end)