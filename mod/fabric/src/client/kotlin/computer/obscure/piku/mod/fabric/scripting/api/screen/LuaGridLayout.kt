package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.GridLayout

class LuaGridLayout(val columns: Int, val screen: CustomScreen) : TwineNative("") {
    val grid = GridLayout()
    val rowHelper = grid.createRowHelper(columns)

    @TwineFunction
    fun add(widget: LuaWidget): LuaGridLayout {
        if (widget.spanColumns > 1) {
            rowHelper.addChild(widget.build(), widget.spanColumns)
        } else {
            rowHelper.addChild(widget.build())
        }
        return this
    }

    @TwineFunction
    fun padding(all: Int): LuaGridLayout {
        grid.defaultCellSetting().padding(all, all, all, 0)
        return this
    }

    @TwineFunction
    fun align(x: Double, y: Double): LuaGridLayout {
        screen.onInit {
            grid.arrangeElements()
            FrameLayout.alignInRectangle(grid, 0, 0, screen.width, screen.height, x.toFloat(), y.toFloat())
            grid.visitWidgets { screen.addWidget(it) }
        }
        return this
    }
}