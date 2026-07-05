
local chat_box = game.ui.column()
    .background(color.rgba(255,255,255,50))
    .width(300)
    .height(400)

function create_message(
    message,
    author,
    author_color
)
    local row = chat_box.addRow()
        .padding(spacing.of(5))
        .gap(5)

    if author ~= nil then
        local author_literal_text = text.literal(author)
        if author_color ~= nil then
            author_literal_text.color(author_color)
        end
        local author_text = row.addText()
            .text(author_literal_text)
    end

    local message_box = row.addText()
        .text(message)
end

listen("piku.demo.chat_message", function(event)
    log.info(event)
    create_message(
        event.message,
        event.author,
        event.author_color
    )
end)