@file:Suppress("DEPRECATION")

package inc.proto.websitemacrorecorder.util

import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebStorage
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.InputStream
import javax.inject.Inject

class Helper @Inject constructor(
    private val webStorage: WebStorage,
    private val cookieManager: CookieManager,
    private val cookieSyncManager: CookieSyncManager
) {
    fun objectToMap(data: Any): Map<String, Any?> {
        val gson = GsonBuilder().serializeNulls().create()
        val json = gson.toJson(data)

        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }

    inline fun <reified T> mapToObject(data: Any?): T {
        val gson = GsonBuilder().serializeNulls().create()
        val json = gson.toJson(data)

        return gson.fromJson(json, T::class.java)
    }

    fun clearStorage() {
        webStorage.deleteAllData()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cookieManager.removeAllCookies(null)
            cookieManager.flush()
        } else {
            cookieSyncManager.startSync()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    fun inputStreamToString(streamStream: InputStream): String {
        return streamStream.bufferedReader().use {
            it.readText()
        }
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
