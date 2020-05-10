package inc.proto.websitemacrorecorder.util

import java.io.InputStream

object Helper {
    fun inputStreamToString(streamStream: InputStream): String {
        return streamStream.bufferedReader().use {
            it.readText()
        }
    }
}
