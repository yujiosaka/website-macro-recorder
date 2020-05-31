package inc.proto.websitemacrorecorder.ui.edit_events_dialog

import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditEventsDialogViewModel : ObservableViewModel() {

    private var _seconds = "1"

    @get:Bindable
    var seconds
        get() = _seconds
        set(value) {
            try {
                val intValue = Integer.parseInt(value)
                if (intValue in MacroEvent.MIN_WAIT_VALUE..MacroEvent.MAX_WAIT_VALUE) {
                    _seconds = value
                }
            } catch (e: NumberFormatException) {
            } finally {
                notifyPropertyChanged(BR.seconds)
            }
        }
}
