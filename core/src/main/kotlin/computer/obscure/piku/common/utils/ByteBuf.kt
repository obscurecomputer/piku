package computer.obscure.piku.common.utils

import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets

fun ByteBuf.writeVarInt(value: Int) {
    var i = value
    while (true) {
        if ((i and 0x7F.inv()) == 0) {
            writeByte(i)
            return
        }
        writeByte((i and 0x7F) or 0x80)
        i = i ushr 7
    }
}

fun ByteBuf.writeString(value: String) {
    val bytes = value.toByteArray(StandardCharsets.UTF_8)
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Int

    do {
        read = readByte().toInt()
        val value = read and 0x7F
        result = result or (value shl (7 * numRead))

        numRead++
        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while ((read and 0x80) != 0)

    return result
}

fun ByteBuf.readString(maxLength: Int = 32767): String {
    val length = readVarInt()
    require(length >= 0) { "Negative string length" }
    require(length <= maxLength * 4) { "String length too large" }

    val bytes = ByteArray(length)
    readBytes(bytes)
    return String(bytes, StandardCharsets.UTF_8)
}
