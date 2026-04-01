

listen("client.key_update", function(event)
    if event.key == "g" and event.action == "press" then
        local storedState = state.get("hi")
        print(storedState.value)

        storedState.onSet = function(newValue)
            client.send("client detected state update: " .. tostring(newValue))
        end
    end
    if event.key == "h" and event.action == "press" then
        client.bobbing = not client.bobbing
    end
    if event.key == "j" and event.action == "press" then
        client.bobbingStrength = 0.1
    end

end)
