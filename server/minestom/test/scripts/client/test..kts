import computer.obscure.piku.core.scheduler.ScheduledTask
import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.fabric.scripting.api.get
import computer.obscure.piku.mod.fabric.scripting.api.menu
import computer.obscure.piku.mod.fabric.ui.classes.Anchor
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.Spacing
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.classes.alignment.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.classes.alignment.MainAxisAlignment
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.SpriteNode
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import me.znotchill.kiwi.generated.Color
import me.znotchill.kiwi.generated.Vec2
import me.znotchill.kiwi.generated.toTextColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import kotlin.math.max

val menu = menu()

data class MenuSizePreset(
    val classSelectScale: Double,
    val classPlayScale: Double,
    val classPlayWidth: Dimension,
    val tooltipWidth: Dimension,
)

val normalMenuSize = MenuSizePreset(
    classSelectScale = 1.24,
    classPlayScale = 2.0,
    classPlayWidth = Dimension.Fixed(250f),
    tooltipWidth = Dimension.Fixed(200f)
)

val smallMenuSize = MenuSizePreset(
    classSelectScale = 1.1,
    classPlayScale = 1.3,
    classPlayWidth = Dimension.Fixed(15f),
    tooltipWidth = Dimension.Fixed(160f)
)
val menuPreset = normalMenuSize

data class Class(
    val name: String,
    val sprite: String,
    val size: Vec2,
    val id: String,
    val role: ClassRole
)

enum class ClassRole {
    OFFENSE,
    DEFENSE,
    SUPPORT
}

val classes = listOf(
    Class(
        name = "Scout",
        sprite = "clanktech:renders/classes/scout_wordmark.png",
        size = Vec2(82, 36),
        id = "scout",
        role = ClassRole.OFFENSE
    )
)

data class Palette(
    val primary: Color,
    val secondary: Color,
    val flash: Color,
    val background: Color,
)

data class Role(
    val role: ClassRole,
    val id: String,
    val prefix: String,
    val adjective: String,
    val description: Component,
    val palette: Palette
)

val roles = listOf(
    Role(
        role = ClassRole.OFFENSE,
        id = "offense",
        prefix = "an",
        adjective = "offensive",
        description = Component.text("hi"),
        palette = palette(17, 74, 55)
    )
)

val role = roles.first()

fun role(role: ClassRole) = roles.first { it.role == role }

fun palette(h: Int, s: Int, l: Int): Palette {
    val base = Color.hsl(h.toFloat(), s.toFloat(), l.toFloat())

    return Palette(
        primary = base,
        secondary = base.darken(0.3f),
        flash = base.lighten(0.2f),
        background = base.darken(0.55f),
    )
}

fun tooltip(event: UIEvent.Hover, node: UINode, texts: List<Component>) {
    val tooltip = menu.get<ColumnNode>("tooltip") ?: menu.column {
        name = "tooltip"
        padding = Spacing(3)
        gap = 2f
    }

    val palette = role.palette
    tooltip.background = palette.background

    texts.forEachIndexed { index, component ->
        val nodeName = "text_$index"
        val node = tooltip.get<TextNode>(nodeName) ?: tooltip.text {
            name = nodeName
            wrap = true
        }
        node.visible = true
        node.text = component
        node.width = menuPreset.tooltipWidth
    }

    val task = ScheduledTask { _ ->
        var widest = 0f
        var height = 0f
        texts.forEachIndexed { index, component ->
            val node = tooltip.get<TextNode>("text_$index")!!
            widest = max(widest, node.measuredWidth)
            height += node.measuredHeight
        }

        val x = node.centerX() - (widest / 2)
        val y = node.layoutY - event.localY - height - 10

        tooltip.offset("${x}px", "${y}px")
        tooltip.visible = true
    }
    task.delay(1)
    Scheduler.submit(
        task
    )
}

fun displayClass(cls: Class) {
    val spriteWidth = Dimension.Fixed(cls.size.x * menuPreset.classPlayScale)
    val spriteHeight = Dimension.Fixed(cls.size.y * menuPreset.classPlayScale)

    val roleInfo = role(cls.role)
    val palette = roleInfo.palette

    val container = menu.get("class_container") ?: menu.column {
        name = "class_container"
        anchor = Anchor.BOTTOM_RIGHT
        crossAxis = CrossAxisAlignment.Center
        mainAxis = MainAxisAlignment.Center
        offset("-5%", "-5%")
    }

    container.width = menuPreset.classPlayWidth

    val role = container.get("role") ?: container.box {
        name = "role"
    }

    role.onHover = { e, n ->
        val event = e as UIEvent.Hover
        tooltip(
            event,
            n,
            listOf(
                Component.text("${cls.name} is ${roleInfo.prefix} ")
                    .append(
                        Component.text(roleInfo.adjective)
                            .color(palette.primary.toTextColor())
                    )
                    .append(
                        Component.text(" class.")
                    ),
                Component.text("As ${roleInfo.prefix} ")
                    .append(
                        Component.text(roleInfo.id)
                            .color(palette.primary.toTextColor())
                    )
                    .append(
                        Component.text(" player, ")
                    )
                    .append(
                        roleInfo.description
                    ),

            )
        )
    }

    val sprite = container.get<SpriteNode>("sprite") ?: container.sprite {
        name = "sprite"
    }
    sprite.texturePath = cls.sprite
    sprite.width = spriteWidth
    sprite.height = spriteHeight

    val roleText = role.get<TextNode>("text") ?: role.text {
        name = "text"
    }
    roleText.text = Component.text(cls.role.name.uppercase())
        .decorate(TextDecoration.BOLD)

    val button = container.get("button") ?: container.text {
        name = "button"
        text = Component.text("CLICK TO PLAY")
            .decorate(TextDecoration.BOLD)
    }

    button.animate {
        background(palette.primary, 0.25, "ease_in_out")
    }.play()
    role.animate {
        background(palette.primary, 0.25, "ease_in_out")
    }.play()

    button.onPress = { _, _ ->
        button.animate {
            background(palette.flash, 0.1, "ease_in_out")
            scale("90%", 0.1, "ease_in_out")
        }.play()
    }
    button.onRelease = { _, _ ->
        button.animate {
            background(palette.primary, 0.1, "ease_in_out")
            scale("100%", 0.1, "ease_in_out")
        }.play()
    }
    button.onHover = { _, _ ->
        button.animate {
            background(palette.secondary, 0.2, "ease_in_out")
        }.play()
    }
    button.onUnhover = { _, _ ->
        button.animate {
            background(palette.primary, 0.2, "ease_in_out")
        }.play()
    }
}

displayClass(classes.first())

menu.open()