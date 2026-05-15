package computer.obscure.piku.core.classes

class Spacing(
    var left: Double = 0.0,
    var top: Double = 0.0,
    var right: Double = 0.0,
    var bottom: Double = 0.0
) {
    constructor(all: Double) : this(all, all, all, all)

    companion object {
        val ZERO = Spacing(0.0)
        val ONE = Spacing(1.0,)
    }
}

val Spacing.horizontal get() = (left + right).toFloat()
val Spacing.vertical get() = (top + bottom).toFloat()
val Spacing.leftF get() = left.toFloat()
val Spacing.topF get() = top.toFloat()
val Spacing.rightF get() = right.toFloat()
val Spacing.bottomF get() = bottom.toFloat()