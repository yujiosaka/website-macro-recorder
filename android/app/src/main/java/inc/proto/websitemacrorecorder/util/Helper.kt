package inc.proto.websitemacrorecorder.util

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.InputStream

object Helper {
    fun inputStreamToString(streamStream: InputStream): String {
        return streamStream.bufferedReader().use {
            it.readText()
        }
    }

    fun objectToMap(data: Any): Map<String, Any?> {
        val gson = GsonBuilder().serializeNulls().create()
        val json = gson.toJson(data)
        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }

    inline fun <reified T> mapToObject(data: Map<String, Any?>): T {
        val gson = GsonBuilder().serializeNulls().create()
        val json = gson.toJson(data)
        return gson.fromJson(json, T::class.java)
    }

    fun <T> throttle(wait: Long, f: (T) -> Unit): (T) -> Unit {
        var job: Job? = null
        return { param: T ->
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                delay(wait)
                f(param)
            }
        }
    }
}
