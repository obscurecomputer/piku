package computer.obscure.piku.common.scripting.base

interface Item {
    val id: String
    val displayName: String?
    val amount: Int
}