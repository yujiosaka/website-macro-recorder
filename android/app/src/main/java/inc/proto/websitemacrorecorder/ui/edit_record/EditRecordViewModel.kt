package inc.proto.websitemacrorecorder.ui.edit_record

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent

class EditRecordViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    fun pushEvent(event: MacroEvent) {
        macro.value!!.events.add(event)
    }

    fun resetEvents() {
        macro.value!!.events = arrayListOf()
    }
}
