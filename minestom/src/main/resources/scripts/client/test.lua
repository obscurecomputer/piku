
listen("client.key_update", function(event)
    if event.key == "b" and event.action == "press" then
        client.send("<yellow>Began holding B! (test)")

        game.camera.toggle()
    end
end)