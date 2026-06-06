package computer.obscure.piku.mod.fabric.storage

import computer.obscure.piku.core.service.PikuService
import computer.obscure.twine.annotations.TwineFunction

interface StorageMethod : PikuService {
    val data: MutableMap<String, Any>

    override fun shutdown() {
        data.clear()
    }

    @TwineFunction
    fun set(k: String, v: Any) {
        data[k] = v
    }

    @TwineFunction
    fun get(k: String) = data[k]

    @TwineFunction
    fun del(k: String) = data.remove(k)
}