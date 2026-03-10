package computer.obscure.piku.core.classes

import java.io.File

sealed interface ScriptSource {
    data class Resource(val path: String) : ScriptSource
    data class Directory(val dir: File) : ScriptSource
}