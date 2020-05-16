package inc.proto.websitemacrorecorder.ui.edit_record

import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.data.Event
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditRecordViewModel(macro: Macro) : ObservableViewModel() {

    private var _macro = macro
    private var _valid = false

    @get:Bindable
    var macro
        get() = _macro
        set(value) {
            _macro = value
            notifyPropertyChanged(BR.macro)
        }

    @get:Bindable
    var url
        get() = _macro.url
        set(value) {
            _macro.url = value
            notifyPropertyChanged(BR.url)
        }

    @get:Bindable
    var name
        get() = _macro.name
        set(value) {
            _macro.name = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var acceptLanguage
        get() = _macro.acceptLanguage
        set(value) {
            _macro.acceptLanguage = value
            notifyPropertyChanged(BR.acceptLanguage)
        }

    @get:Bindable
    var userAgent
        get() = _macro.userAgent
        set(value) {
            _macro.userAgent = value
            notifyPropertyChanged(BR.userAgent)
        }

    @get:Bindable
    var valid
        get() = _valid
        set(value) {
            _valid = value
            notifyPropertyChanged(BR.valid)
        }

    fun pushEvent(event: Event) {
        _macro.events.add(event)
    }

    fun resetEvents() {
        _macro.events = arrayListOf()
    }
}
