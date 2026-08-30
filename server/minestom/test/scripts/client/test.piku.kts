import computer.obscure.piku.mod.fabric.scripting.api.ui
import computer.obscure.piku.mod.fabric.ui.classes.Anchor
import computer.obscure.piku.mod.fabric.utils.parseDimension
import me.znotchill.kiwi.generated.Color

ui().box {
    background = Color.hsl(100f, 10f, 50f)
    width = parseDimension("10%")
    height = parseDimension("10%")
    anchor = Anchor.CENTER_CENTER
}