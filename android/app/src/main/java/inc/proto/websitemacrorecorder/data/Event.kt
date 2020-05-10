package inc.proto.websitemacrorecorder.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var name: String = "",
    var value: String  = "",
    var xPath: String = "",
    var cssSelector: String = ""
) : Parcelable
