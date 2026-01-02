package me.znotchill.piku.common.scripting.base

import computer.obscure.twine.nativex.TwineNative

abstract class Player : TwineNative() {

    abstract val uuid: String
    abstract val username: String

    abstract fun send(message: String)
}