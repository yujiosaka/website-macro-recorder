package inc.proto.websitemacrorecorder.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var name: String = "",
    var xPath: String = "",
    var targetType: String = "",
    var value: String  = ""
) : Parcelable {
    companion object {
        const val MIN_WAIT_VALUE = 1
        const val MAX_WAIT_VALUE = 30
    }
}
