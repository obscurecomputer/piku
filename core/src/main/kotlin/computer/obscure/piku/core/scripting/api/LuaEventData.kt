package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaEventData(private val fields: MutableMap<String, Any?> = mutableMapOf()) : TwineNative("eventData") {

    constructor(vararg pairs: Pair<String, Any?>) : this(mutableMapOf(*pairs))

    operator fun get(key: String): Any? = fields[key]
    operator fun set(key: String, value: Any?) { fields[key] = value }

    @TwineFunction("get")
    fun getField(key: String): Any? = fields[key]

    @TwineFunction
    fun set(key: String, value: String) { fields[key] = value }

    @TwineFunction
    fun has(key: String): Boolean = fields.containsKey(key)
}