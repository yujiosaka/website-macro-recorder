package inc.proto.websitemacrorecorder.ui.dialog.edit_events_dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.MacroEvent

class EditEventsDialogViewModel : ViewModel() {
    var seconds = MutableLiveData<String>().also {
        it.value = MacroEvent.DEFAULT_WAIT_VALUE.toString()
    }
}
