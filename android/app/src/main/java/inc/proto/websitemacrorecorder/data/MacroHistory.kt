package inc.proto.websitemacrorecorder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import inc.proto.websitemacrorecorder.BR
import java.io.Serializable

class MacroHistory(
    screenshotUrl: String = "",
    isEntirePageUpdated: Boolean = false,
    isSelectedAreaUpdated: Boolean = false,
    isFailure: Boolean = false,
    executedAt: Timestamp? = null
) : Serializable, BaseObservable() {
    @Bindable
    var screenshotUrl = screenshotUrl
        set(value) {
            field = value
            notifyPropertyChanged(BR.screenshotUrl)
        }

    @Bindable
    var isEntirePageUpdated = isEntirePageUpdated
        set(value) {
            field = value
            notifyPropertyChanged(BR.isEntirePageUpdated)
        }

    @Bindable
    var isSelectedAreaUpdated = isSelectedAreaUpdated
        set(value) {
            field = value
            notifyPropertyChanged(BR.isSelectedAreaUpdated)
        }

    @Bindable
    var isFailure = isFailure
        set(value) {
            field = value
            notifyPropertyChanged(BR.isFailure)
        }

    @Bindable
    var executedAt = executedAt
        set(value) {
            field = value
            notifyPropertyChanged(BR.executedAt)
        }
}
