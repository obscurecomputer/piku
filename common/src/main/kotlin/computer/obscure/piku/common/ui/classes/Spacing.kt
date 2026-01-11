package computer.obscure.piku.common.ui.classes

class Spacing(
    var left: Float = 0f,
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f
) {
    constructor(all: Float) : this(all, all, all, all)
}
