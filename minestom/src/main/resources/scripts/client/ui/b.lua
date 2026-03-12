

listen("client.key_update", function(event)
    if event.key == "g" and event.action == "press" then
        local storedState = state.get("hi")
        print(storedState.value)
    end
end)