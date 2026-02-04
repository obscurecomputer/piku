package computer.obscure.piku.core.scripting.base

open class LuaEvent {
    open val id: String = ""
    open val onClientReceive: () -> Unit = {}
    open val onClientSend: () -> Unit = {}
    open val onServerReceive: () -> Unit = {}
    open val onServerSend: () -> Unit = {}
}