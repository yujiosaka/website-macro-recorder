package inc.proto.websitemacrorecorder.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Macro(
    var _id: String? = null,
    var url: String = "https://",
    var title: String = "",
    var scheduleFrequency: Int = 0,
    var scheduleHour: Int = 10,
    var scheduleMinute: Int = 0,
    var notifySuccess: Boolean = true,
    var notifyFailure: Boolean = true,
    var acceptLanguage: String = "en",
    var userAgent: String = ""
) : Parcelable