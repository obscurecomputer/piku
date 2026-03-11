

listen("client.update_state", function(state)
    client.send("received state update for " .. state.name)
    state.set("test")
end)