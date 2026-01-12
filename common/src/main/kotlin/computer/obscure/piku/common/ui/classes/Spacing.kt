package computer.obscure.piku.common.ui.classes

class Spacing(
    var left: Double = 0.0,
    var top: Double = 0.0,
    var right: Double = 0.0,
    var bottom: Double = 0.0
) {
    constructor(all: Double) : this(all, all, all, all)
}
