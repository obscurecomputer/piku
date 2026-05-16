game.ui.clear()

local root = game.ui.column()
root.width("300px")
root.height("wrap")
root.padding(spacing.of(10))
root.background(color.hex("#cc111111"))
root.gap(8)
root.anchor("top_left")
root.offset(vec2.of(10, 10))

local header = root.addRow()
header.width("fill")
header.mainAxis("spaceBetween")
header.crossAxis("center")
header.padding(spacing.of(6))
header.background(color.hex("#ff222222"))
header.gap(4)
header.addText()
    .text("Scroll Test")
    .color(color.hex("#ffdd44")).shadow(true)
header.addText()
    .text("↕").color(color.hex("#888888"))

local list = root.addColumn()
list.width("fill")
list.height("120px")
list.background(color.hex("#aa223322"))
list.padding(spacing.of(4))
list.gap(3)
list.scrollable(true)

for i = 1, 20 do
    local row = list.addRow()
    row.width("fill")
    row.mainAxis("spaceBetween")
    row.crossAxis("center")
    row.background(color.hex("#44ffffff"))
    row.padding(spacing.of(4))

    row.addText().text("Item " .. i).color(color.white())
    row.addText().text("#" .. i).color(color.hex("#888888"))
end

local hlist = root.addRow()
hlist.width("fill")
hlist.height("40px")
hlist.background(color.hex("#aa222244"))
hlist.padding(spacing.of(4))
hlist.gap(6)
hlist.scrollable(true)

for i = 1, 15 do
    local box = hlist.addBox()
    box.width("50px")
    box.height("fill")
    box.background(color.hex("#55" .. string.format("%02x", (i * 17) % 256) .. "88ff"))

    box.addText()
        .text(tostring(i))
        .anchor("center_center")
        .color(color.white())
        .shadow(true)
end

local outer = root.addColumn()
outer.width("fill")
outer.height("80px")
outer.background(color.hex("#aa332211"))
outer.padding(spacing.of(4))
outer.gap(4)
outer.scrollable(true)

for i = 1, 5 do
    local innerRow = outer.addRow()
    innerRow.width("fill")
    innerRow.height("40px")
    innerRow.background(color.hex("#aa443322"))
    innerRow.gap(4)
    innerRow.scrollable(true)

    for j = 1, 10 do
        local cell = innerRow.addBox()
        cell.width("30px")
        cell.height("fill")
        cell.background(color.hex("#66ff" .. string.format("%02x", j * 25) .. "44"))

        cell.addText()
            .text(i .. "," .. j)
            .anchor("center_center")
            .color(color.white())
    end
end

local containers = {list, hlist, outer}
local selected = 1

local function update_selection()
    for i, c in ipairs(containers) do
        if i == selected then
            c.background(color.hex("#aa223388"))
        else
            c.background(color.hex("#aa223322"))
        end
    end
    hlist.background(i == 2 and color.hex("#aa222288") or color.hex("#aa222244"))
    outer.background(i == 3 and color.hex("#aa332288") or color.hex("#aa332211"))
end

local backgrounds = {
    color.hex("#aa223388"),
    color.hex("#aa222288"),
    color.hex("#aa332288"),
}
local defaults = {
    color.hex("#aa223322"),
    color.hex("#aa222244"),
    color.hex("#aa332211"),
}

local function update_selection()
    for i, c in ipairs(containers) do
        c.background(i == selected and backgrounds[i] or defaults[i])
    end
end

update_selection()

listen("client.mouse_update", function(event)
    if event.action == "press" and event.button == 0 then
        selected = (selected % #containers) + 1
        update_selection()
    end
end)

local scrollTargets = {0, 0, 0}

listen("client.scroll", function(event)
    scrollTargets[selected] = scrollTargets[selected] + event.deltaY * -12
    scrollTargets[selected] = math.max(0, math.min(scrollTargets[selected], containers[selected].maxScroll))
    containers[selected].animate()
        .scroll(scrollTargets[selected], 0.15, "EASE_OUT")
        .play()
end)