

function create_user_card(
    avatar,
    username,
    performance,
    accuracy,
    level,
    level_progress,
    rank
)
    local container = game.ui.row()
        .background(color.rgb(0,0,0))
        .padding(spacing.of(5))
        .width(300)
        .anchor("center_center")
        .gap(5)

    container.addSprite()
        .texture(avatar)
        .width(60)
        .height(60)

    local row = container.addRow()
        .gap(-50)

    local info_col = row.addColumn()
        .gap(5)
        .width(125)

    info_col.addText()
        .text(username)
        .scale("2x")
    info_col.addText()
        .text("Performance: " .. performance .. "pp")
        .scale("1x")
    info_col.addText()
        .text("Accuracy: " .. accuracy .. "%")
        .scale("1x")
    info_col.addText()
        .text("Lv" .. level)
        .scale("1x")

    local rankDisplay = row.addColumn()
        .mainAxis("end")
        .crossAxis("end")
        .height("60")
    rankDisplay.addText()
        .text("#" .. rank)
        .scale("4x")
        .color(color.rgba(171, 171, 171, 120))
    rankDisplay.addProgressBar()
        .value(level_progress)
        .height(5)
        .width("150")
        .track(color.rgba(171, 171, 171, 50))
        .foreground(color.rgba(245, 209, 66, 200))
end

create_user_card(
    "minecraft:textures/item/diamond.png",
    "znci",
    32048,
    96.01,
    97,
    0.8,
    18729
)