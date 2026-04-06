package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.twine.nativex.conversion.Converter.toKotlinType
import computer.obscure.twine.nativex.conversion.Converter.toKotlinValue
import org.luaj.vm2.LuaTable

class LuaLogger(
    val scriptName: String
) : TwineNative("log") {
    @TwineNativeFunction
    fun info(message: Any) {
        println(
            getMessage(LogLevel.INFO, message)
        )
    }

    @TwineNativeFunction
    fun warn(message: Any) {
        println(
            getMessage(LogLevel.WARN, message)
        )
    }

    @TwineNativeFunction
    fun error(message: Any) {
        println(
            getMessage(LogLevel.ERROR, message)
        )
    }

    fun getMessage(level: LogLevel, message: Any): String {
        val fixedMessage = when (message) {
            is LuaTable -> {
                val actualValue = message.toKotlinValue(message.toKotlinType())
                println(actualValue)
                println("hello")
                "hi"
            }
            else -> message
        }
        return "[$level] $scriptName -- $fixedMessage"
    }

    enum class LogLevel {
        INFO,
        WARN,
        ERROR
    }
}