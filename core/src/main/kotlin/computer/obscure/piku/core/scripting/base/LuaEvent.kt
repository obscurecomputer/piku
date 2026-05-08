package computer.obscure.piku.core.scripting.base

open class LuaEvent {
    open val id: String = ""
    open val onClientReceive: (data: Any) -> Unit = {}
    open val onClientSend: (data: Any) -> Unit = {}
    open val onServerReceive: (data: Any) -> Unit = {}
    open val onServerSend: (data: Any) -> Unit = {}
}