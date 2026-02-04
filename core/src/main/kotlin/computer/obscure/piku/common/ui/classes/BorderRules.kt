package computer.obscure.piku.common.ui.classes

data class BorderRules(
    var width: Float = 0f,
    var color: UIColor = UIColor(0, 0, 0, 255),
    var position: BorderPosition = BorderPosition.INSIDE
)

enum class BorderPosition {
    INSIDE,
    OUTSIDE
}