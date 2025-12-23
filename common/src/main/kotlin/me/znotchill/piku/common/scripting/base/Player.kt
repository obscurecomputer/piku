package me.znotchill.piku.common.scripting.base

interface Player {
    fun send(message: String)
    val uuid: String
}