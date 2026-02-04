package computer.obscure.piku.core.scripting.engine

class EngineError(
    val code: EngineErrorCode,
    message: String
) : RuntimeException(message)
