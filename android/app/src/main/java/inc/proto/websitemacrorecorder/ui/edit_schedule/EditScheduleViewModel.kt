package inc.proto.websitemacrorecorder.ui.edit_schedule

import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditScheduleViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro

    @get:Bindable
    var macro
        get() = _macro
        set(value) {
            _macro = value
            notifyPropertyChanged(BR.macro)
        }

    @get:Bindable
    var scheduleFrequency
        get() = _macro.scheduleFrequency
        set(value) {
            _macro.scheduleFrequency = value
            notifyPropertyChanged(BR.scheduleFrequency)
        }

    @get:Bindable
    var scheduleHour
        get() = _macro.scheduleHour
        set(value) {
            _macro.scheduleHour = value
            notifyPropertyChanged(BR.scheduleHour)
            notifyPropertyChanged(BR.schedule)
        }

    @get:Bindable
    var scheduleMinute
        get() = _macro.scheduleMinute
        set(value) {
            _macro.scheduleMinute = value
            notifyPropertyChanged(BR.scheduleMinute)
            notifyPropertyChanged(BR.schedule)
        }

    @get:Bindable
    var schedule: String
        get() = "%1$01d:%2$02d".format(_macro.scheduleHour, _macro.scheduleMinute)
        set(value) {
            val pair = value.split(":").map { it.toInt() }
            _macro.scheduleHour = pair[0]
            _macro.scheduleMinute = pair[1]
            notifyPropertyChanged(BR.scheduleHour)
            notifyPropertyChanged(BR.scheduleMinute)
            notifyPropertyChanged(BR.schedule)
        }
}
