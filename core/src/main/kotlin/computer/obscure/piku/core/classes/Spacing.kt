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