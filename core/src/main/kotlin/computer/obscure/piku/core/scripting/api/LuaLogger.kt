package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaLogger(
    val scriptName: String
) : TwineNative("log") {
    @TwineFunction
    fun info(message: Any) {
        println(
            getMessage(LogLevel.INFO, message)
        )
    }

    @TwineFunction
    fun warn(message: Any) {
        println(
            getMessage(LogLevel.WARN, message)
        )
    }

    @TwineFunction
    fun error(message: Any) {
        println(
            getMessage(LogLevel.ERROR, message)
        )
    }

    fun getMessage(level: LogLevel, message: Any): String {
        val fixedMessage = when (message) {
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