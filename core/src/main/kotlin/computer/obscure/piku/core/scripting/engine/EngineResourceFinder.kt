package computer.obscure.piku.core.scripting.engine

import org.luaj.vm2.lib.ResourceFinder
import java.io.ByteArrayInputStream
import java.io.InputStream

class EngineResourceFinder(private val scripts: Map<String, String>) : ResourceFinder {
    override fun findResource(filename: String): InputStream? {
        var content = scripts[filename]

        if (content == null) {
            content = scripts[filename.removeSuffix(".lua")]
        }

        if (content == null) {
            val dotted = filename.removeSuffix(".lua").replace("/", ".")
            content = scripts[dotted]
        }

        return content?.let { ByteArrayInputStream(it.toByteArray()) }
    }
}