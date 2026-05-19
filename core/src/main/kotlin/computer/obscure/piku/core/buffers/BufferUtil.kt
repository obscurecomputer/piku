package computer.obscure.piku.core.buffers

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun ByteArrayOutputStream.writeVarIntString(value: String) {
    val bytes = value.toByteArray(Charsets.UTF_8)
    this.writeVarInt(bytes.size)
    this.write(bytes)
}

fun ByteArrayOutputStream.writeVarInt(value: Int) {
    var v = value
    while (true) {
        if (v and 0x7F.inv() == 0) {
            this.write(v)
            break
        }
        this.write((v and 0x7F) or 0x80)
        v = v ushr 7
    }
}

fun ByteArrayInputStream.readVarInt(): Int {
    var result = 0; var shift = 0; var b: Int
    do {
        b = read()
        result = result or ((b and 0x7F) shl shift)
        shift += 7
    } while (b and 0x80 != 0)
    return result
}

fun ByteArrayInputStream.readVarIntString(): String {
    val len = readVarInt()
    return String(readNBytes(len), Charsets.UTF_8)
}