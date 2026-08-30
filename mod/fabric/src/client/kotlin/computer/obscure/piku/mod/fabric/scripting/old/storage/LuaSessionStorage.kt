package computer.obscure.piku.mod.fabric.scripting.old.storage

import computer.obscure.piku.mod.fabric.storage.SessionStorage
import computer.obscure.piku.mod.fabric.storage.StorageMethod
import computer.obscure.twine.TwineNative

open class LuaSessionStorage : TwineNative("session"), StorageMethod {
    override val data get() = SessionStorage.data
}