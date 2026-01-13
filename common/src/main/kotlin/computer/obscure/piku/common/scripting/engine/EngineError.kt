package computer.obscure.piku.common.scripting.engine

class EngineError(
    val code: EngineErrorCode,
    message: String
) : RuntimeException(message)
