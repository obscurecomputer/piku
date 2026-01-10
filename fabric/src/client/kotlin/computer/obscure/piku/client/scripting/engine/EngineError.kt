package computer.obscure.piku.client.scripting.engine

class EngineError(
    val code: EngineErrorCode,
    message: String
) : RuntimeException(message)
