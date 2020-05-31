package inc.proto.websitemacrorecorder.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

object Helper {
    fun inputStreamToString(streamStream: InputStream): String {
        return streamStream.bufferedReader().use {
            it.readText()
        }
    }

    fun objectToMap(data: Any): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(data)
        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }

    inline fun <reified T> mapToObject(data: Map<String, Any?>): T {
        val gson = Gson()
        val json = gson.toJson(data)
        return gson.fromJson(json, T::class.java)
    }
}
