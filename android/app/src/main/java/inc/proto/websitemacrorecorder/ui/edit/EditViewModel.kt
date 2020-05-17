package inc.proto.websitemacrorecorder.ui.edit

import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro

    @get:Bindable
    var name
        get() = _macro.name
        set(value) {
            _macro.name = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var url
        get() = _macro.url
        set(value) {
            _macro.url = value
            notifyPropertyChanged(BR.url)
        }

    @get:Bindable
    var scheduleFrequency
        get() = _macro.scheduleFrequency
        set(value) {
            _macro.scheduleFrequency = value
            notifyPropertyChanged(BR.scheduleFrequency)
        }

    @get:Bindable
    var notifySuccess
        get() = _macro.notifySuccess
        set(value) {
            _macro.notifySuccess = value
            notifyPropertyChanged(BR.notifySuccess)
        }

    @get:Bindable
    var notifyFailure
        get() = _macro.notifyFailure
        set(value) {
            _macro.notifyFailure = value
            notifyPropertyChanged(BR.notifyFailure)
        }

    @get:Bindable
    var schedule: String
        get() = "%1$01d:%2$02d".format(_macro.scheduleHour, _macro.scheduleMinute)
        set(value) {
            val pair = value.split(":").map { it.toInt() }
            _macro.scheduleHour = pair[0]
            _macro.scheduleMinute = pair[1]
            notifyPropertyChanged(BR.schedule)
        }
}
