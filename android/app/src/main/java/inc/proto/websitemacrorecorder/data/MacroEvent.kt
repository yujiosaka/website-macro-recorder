package inc.proto.websitemacrorecorder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.BR
import java.io.Serializable

class MacroEvent(
    name: String = "",
    xPath: String = "",
    targetType: String = "",
    value: String  = "",
    isError: Boolean = false
) : Serializable, BaseObservable() {
    companion object {
        const val DEFAULT_WAIT_VALUE = 3
        const val MIN_WAIT_VALUE = 1
        const val MAX_WAIT_VALUE = 30
    }

    @Bindable
    var name = name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var xPath = xPath
        set(value) {
            field = value
            notifyPropertyChanged(BR.xPath)
        }

    @Bindable
    var targetType = targetType
        set(value) {
            field = value
            notifyPropertyChanged(BR.targetType)
        }

    @Bindable
    var value = value
        set(value) {
            field = value
            notifyPropertyChanged(BR.value)
        }

    @Bindable
    var isError = isError
        set(value) {
            field = value
            notifyPropertyChanged(BR.isError)
        }
}
