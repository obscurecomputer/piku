game.ui.clear()

local screen = game.ui.row()
screen.width("100%")
screen.height("100%")
screen.gap(0)
screen.anchor("top_left")

-- left panel
local leftSection = screen.addRow()
leftSection.width("fill")
leftSection.height("fill")
leftSection.gap(0)

local left = leftSection.addColumn()
left.width("fill")
left.height("fill")
left.background(color.hex("#ee0d0d12"))
left.padding(spacing.of(12))
left.gap(10)
left.scrollable(true)

-- left scrollbar
local leftScrollbar = leftSection.addScrollbar()
leftScrollbar.direction("vertical")
leftScrollbar.width("3px")
leftScrollbar.height("fill")
leftScrollbar.target(left)
leftScrollbar.trackColor(color.hex("#11ffffff"))
leftScrollbar.thumbColor(color.hex("#55ffffff"))

-- center panel
local centerSection = screen.addRow()
centerSection.width("fill")
centerSection.height("fill")
centerSection.gap(0)

local center = centerSection.addColumn()
center.width("fill")
center.height("fill")
center.background(color.hex("#ee080810"))
center.padding(spacing.of(12))
center.gap(10)
center.scrollable(true)

local centerScrollbar = centerSection.addScrollbar()
centerScrollbar.direction("vertical")
centerScrollbar.width("3px")
centerScrollbar.height("fill")
centerScrollbar.target(center)
centerScrollbar.trackColor(color.hex("#11ffffff"))
centerScrollbar.thumbColor(color.hex("#55ffffff"))

local rightSection = screen.addRow()
rightSection.width("fill")
rightSection.height("fill")
rightSection.gap(0)

local right = rightSection.addColumn()
right.width("fill")
right.height("fill")
right.background(color.hex("#ee0d120d"))
right.padding(spacing.of(12))
right.gap(10)
right.scrollable(true)

local rightScrollbar = rightSection.addScrollbar()
rightScrollbar.direction("vertical")
rightScrollbar.width("3px")
rightScrollbar.height("fill")
rightScrollbar.target(right)
rightScrollbar.trackColor(color.hex("#11ffffff"))
rightScrollbar.thumbColor(color.hex("#55ffffff"))

-- section: text styles
local textHeader = left.addRow()
textHeader.width("fill")
textHeader.mainAxis("spaceBetween")
textHeader.crossAxis("center")
textHeader.addText().text("BASIC TEXT STYLES").color(color.hex("#ffdd44")).shadow(true)

left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

left.addText().text("plain white text").color(color.white())
left.addText().text("shadowed text").color(color.hex("#aaffaa")).shadow(true)
left.addText().text(
    text.literal("bold ")
        .bold()
        .append(text.literal("italic ")
            .italic())
        .append(text.literal("underline")
            .underline())
).color(color.white()).shadow(true)

left.addText().text(
    text.literal("red ")
        .color(color.hex("#ff4444"))
        .append(text.literal("green ")
            .color(color.hex("#44ff44")))
        .append(text.literal("blue")
            .color(color.hex("#4444ff")))
).shadow(true)

left.addText().text(
    text.literal("RAINBOW ")
        .color(color.hsl(0, 100, 60))
        .append(text.literal("T")
            .color(color.hsl(30, 100, 60)))
        .append(text.literal("E")
            .color(color.hsl(60, 100, 60)))
        .append(text.literal("X")
            .color(color.hsl(120, 100, 60)))
        .append(text.literal("T")
            .color(color.hsl(180, 100, 60)))
        .append(text.literal("!")
            .color(color.hsl(240, 100, 60)))
)

left.addDivider().width("fill").color(color.hex("#22ffffff")).thickness(1)

local textHeader = left.addRow()
textHeader.width("fill")
textHeader.mainAxis("spaceBetween")
textHeader.crossAxis("center")
textHeader.addText().text("INTERPOLATION").color(color.hex("#ffdd44")).shadow(true)

left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

left.addText().text("Hi {PLAYER_NAME}").color(color.white())
left.addText().text("FPS: {CLIENT_FPS}").color(color.random())

left.addDivider().width("fill").color(color.hex("#22ffffff")).thickness(1)

-- section: progress bars
left.addText().text("PROGRESS BARS").color(color.hex("#ffdd44")).shadow(true)
left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

local function make_bar(parent, label, value, fill, track)
    local row = parent.addRow()
    row.width("fill")
    row.crossAxis("center")
    row.gap(6)

    row.addText().text(label).color(color.hex("#aaaaaa")).width("20%")

    local barSection = row.addColumn()
    barSection.width("fill")
    barSection.gap(2)

    local bar = barSection.addProgressBar()
    bar.width("fill")
    bar.height("8px")
    bar.value(value)
    bar.foreground(fill)
    bar.track(track or fill.darken(0.4))

    bar.addText()
        .text(math.floor(value * 100) .. "%")
        .anchor("center_right")
        .color(color.white())
        .shadow(true)
        .padding(spacing.of(0, 3, 0, 0))
end

make_bar(left, "HP", 0.72, color.hsl(0, 70, 55))
make_bar(left, "ÜBER", 0.45, color.hsl(220, 70, 60))
make_bar(left, "XP", 0.91, color.hsl(60, 80, 55))
make_bar(left, "OVERHEAL", 0.33, color.hsl(280, 60, 60))

left.addDivider().width("fill").color(color.hex("#22ffffff"))

-- section: gradients
left.addText().text("BASIC GRADIENTS").color(color.hex("#ffdd44")).shadow(true)
left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

local g1 = left.addGradient()
g1.width("fill")
g1.height("20px")
g1.from(color.hsl(0, 80, 50))
g1.to(color.hsl(60, 80, 50))
g1.direction("horizontal")

local g2 = left.addGradient()
g2.width("fill")
g2.height("20px")
g2.from(color.hsl(120, 80, 50))
g2.to(color.hsl(240, 80, 50))
g2.direction("horizontal")

local g3 = left.addGradient()
g3.width("fill")
g3.height("20px")
g3.from(color.hex("#ff000000"))
g3.to(color.hex("#00000000"))
g3.direction("horizontal")

left.addDivider().width("fill").color(color.hex("#22ffffff"))

-- section: sprites + anchoring
left.addText().text("SPRITES W/ ANCHORS").color(color.hex("#ffdd44")).shadow(true)
left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

local spriteBox = left.addBox()
spriteBox.width("fill")
spriteBox.height("48px")
spriteBox.background(color.hex("#33ffffff"))

local items = {
    "minecraft:textures/item/diamond.png",
    "minecraft:textures/item/emerald.png",
    "minecraft:textures/item/gold_ingot.png",
    "minecraft:textures/item/iron_ingot.png",
}
local anchors = {"top_left", "top_right", "bottom_left", "bottom_right"}
for i, tex in ipairs(items) do
    spriteBox.addSprite()
        .texture(tex)
        .anchor(anchors[i])
        .size(vec2.of(16, 16))
end
spriteBox.addText()
    .text("center")
    .anchor("center_center")
    .color(color.white())
    .shadow(true)

left.addDivider().width("fill").color(color.hex("#22ffffff"))

left.addText().text("NESTED LAYOUT").color(color.hex("#ffdd44")).shadow(true)
left.addDivider().width("fill").color(color.hex("#33ffdd44")).thickness(1)

local nestedRow = left.addRow()
nestedRow.width("fill")
nestedRow.gap(4)

local cols = {
    {label = "COL A", bg = "#33ff4444"},
    {label = "COL B", bg = "#3344ff44"},
    {label = "COL C", bg = "#334444ff"},
}
for _, def in ipairs(cols) do
    local col = nestedRow.addColumn()
    col.width("fill")
    col.background(color.hex(def.bg))
    col.padding(spacing.of(4))
    col.gap(3)
    col.addText().text(def.label).color(color.white()).shadow(true)
    col.addDivider().width("fill").color(color.hex("#44ffffff"))
    for i = 1, 3 do
        col.addText().text("lorem ipsum " .. i).color(color.hex("#aaaaaa"))
    end
end

local centerHeader = center.addRow()
centerHeader.width("fill")
centerHeader.mainAxis("center")
centerHeader.crossAxis("center")
centerHeader.gap(8)
centerHeader.addText()
    .text("DEMOS")
    .color(color.hex("#ffffff"))
    .shadow(true)

center.addDivider().width("fill").color(color.hex("#33ffffff"))

-- alignment showcase
center.addText().text("AXIS ALIGNMENTS").color(color.hex("#ffdd44")).shadow(true)

local alignDefs = {
    {main = "start",        label = "start"},
    {main = "center",       label = "center"},
    {main = "end",          label = "end"},
    {main = "spaceBetween", label = "space_between"},
    {main = "spaceAround",  label = "space_around"},
}

for _, def in ipairs(alignDefs) do
    local section = center.addColumn()
    section.width("fill")
    section.gap(2)

    section.addText().text(def.label).color(color.hex("#888888"))

    local row = section.addRow()
    row.width("fill")
    row.height("24px")
    row.mainAxis(def.main)
    row.crossAxis("center")
    row.background(color.hex("#22ffffff"))
    row.gap(4)

    for i = 1, 4 do
        local box = row.addBox()
        box.width("20px")
        box.height("14px")
        box.background(color.hsl(i * 60, 70, 55))
        box.addText()
            .text(tostring(i))
            .anchor("center_center")
            .color(color.white())
    end
end

center.addDivider().width("fill").color(color.hex("#33ffffff"))

center.addText().text("CROSS ALIGNMENTS").color(color.hex("#ffdd44")).shadow(true)

local crossDefs = {"start", "center", "end"}
for _, cross in ipairs(crossDefs) do
    local section = center.addColumn()
    section.width("fill")
    section.gap(2)
    section.addText().text(cross).color(color.hex("#888888"))

    local row = section.addRow()
    row.width("fill")
    row.height("40px")
    row.mainAxis("start")
    row.crossAxis(cross)
    row.background(color.hex("#22ffffff"))
    row.gap(4)
    row.padding(spacing.of(4))

    local heights = {14, 24, 18, 30}
    for i, h in ipairs(heights) do
        local box = row.addBox()
        box.width("30px")
        box.height(h .. "px")
        box.background(color.hsl(i * 80, 60, 50))
    end
end

center.addDivider().width("fill").color(color.hex("#33ffffff"))

-- fraction dimensions showcase
center.addText().text("FRACTION DIMENSIONS").color(color.hex("#ffdd44")).shadow(true)

local fracRow = center.addRow()
fracRow.width("fill")
fracRow.height("30px")
fracRow.gap(2)

local fracs = {
    {w = "10%",  c = "#ff4444"},
    {w = "20%",  c = "#ff8844"},
    {w = "30%",  c = "#ffdd44"},
    {w = "40%",  c = "#44ff44"},
}
for _, f in ipairs(fracs) do
    local box = fracRow.addBox()
    box.width(f.w)
    box.height("fill")
    box.background(color.hex(f.c))
    box.addText()
        .text(f.w)
        .anchor("center_center")
        .color(color.black())
end

right.addText().text("INVENTORY GRID").color(color.hex("#ffdd44")).shadow(true)
right.addDivider().width("fill").color(color.hex("#33ffdd44"))

local textures = {
    "minecraft:textures/item/diamond.png",
    "minecraft:textures/item/emerald.png",
    "minecraft:textures/item/gold_ingot.png",
    "minecraft:textures/item/iron_ingot.png",
    "minecraft:textures/item/coal.png",
}

local COLS = 4
for row_i = 1, 8 do
    local gridRow = right.addRow()
    gridRow.width("fill")
    gridRow.gap(3)
    gridRow.mainAxis("start")

    for col_i = 1, COLS do
        local idx = ((row_i - 1) * COLS + col_i - 1) % #textures + 1
        local slot = gridRow.addBox()
        slot.width("30px")
        slot.height("30px")
        slot.background(color.hex("#33ffffff"))

        slot.addSprite()
            .texture(textures[idx])
            .size(vec2.of(16, 16))
            .anchor("center_center")

        slot.addText()
            .text(tostring((row_i - 1) * COLS + col_i))
            .anchor("bottom_right")
            .color(color.white())
            .shadow(true)
            .offset(vec2.of(-2, -1))
    end
end

right.addDivider().width("fill").color(color.hex("#22ffffff"))

-- animated bars section
right.addText().text("BARS").color(color.hex("#ffdd44")).shadow(true)
right.addDivider().width("fill").color(color.hex("#33ffdd44"))

local animBars = {}
local animDefs = {
    {label = "ping",  fill = color.hsl(120, 70, 55)},
    {label = "cpu",   fill = color.hsl(30,  70, 55)},
    {label = "mem",   fill = color.hsl(200, 70, 55)},
    {label = "fps",   fill = color.hsl(280, 70, 55)},
}

for _, def in ipairs(animDefs) do
    local row = right.addRow()
    row.width("fill")
    row.crossAxis("center")
    row.gap(6)

    row.addText().text(def.label).color(color.hex("#888888")).width("30px")

    local bar = row.addProgressBar()
    bar.name(def.label .. "_bar")
    bar.width("fill")
    bar.height("6px")
    bar.value(0)
    bar.foreground(def.fill)
    bar.track(def.fill.darken(0.4))

    table.insert(animBars, {bar = bar, fill = def.fill})
end

-- animate bars with fake data
local barTargets = {}
for i = 1, #animBars do
    barTargets[i] = math.random(20, 90) / 100
end

local function animate_bars()
    for i, entry in ipairs(animBars) do
        entry.bar.animate()
            .progress(barTargets[i], 1.0, "EASE_IN_OUT_QUINT")
            .play()
    end
end

animate_bars()

scheduler.task(function()
    for i = 1, #animBars do
        barTargets[i] = math.random(10, 95) / 100
    end
    animate_bars()
end).loop(20).run()

local scrollables = {
    {node = left,  target = 0},
    {node = center,  target = 0},
    {node = right, target = 0},
}

local selectedBg   = color.hex("#33ffffff")
local unselectedBg = color.hex("#ee0d0d12")

local function update_selected_panel()
    left.background(scrollSelected == 1 and selectedBg or color.hex("#ee0d0d12"))
    center.background(scrollSelected == 2 and selectedBg or color.hex("#ee080810"))
    right.background(scrollSelected == 3 and selectedBg or color.hex("#ee0d120d"))
end

local scrollSelected = 1

update_selected_panel()

listen("client.mouse_update", function(event)
    if event.action == "press" and event.button == 0 then
        scrollSelected = (scrollSelected % #scrollables) + 1
        update_selected_panel()
    end
end)

listen("client.scroll", function(event)
    local s = scrollables[scrollSelected]
    s.target = s.target + event.deltaY * -12
    s.target = math.max(0, math.min(s.target, s.node.maxScroll))
    s.node.animate()
        .scroll(s.target, 0.15, "EASE_OUT")
        .play()
end)