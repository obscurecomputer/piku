package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

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
        return "[$level] $scriptName -- $message"
    }

    enum class LogLevel {
        INFO,
        WARN,
        ERROR
    }
}