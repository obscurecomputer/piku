

listen("client.key_update", function(event)
    if event.key == "g" and event.action == "press" then
        local storedState = state.get("hi")
        print(storedState.value)

        storedState.onSet = function(newValue)
            client.send("client detected state update: " .. tostring(newValue))
        end
    end

    client.getKeybind("key.jump").once()
end)