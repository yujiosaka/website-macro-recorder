package inc.proto.websitemacrorecorder.ui.edit_record

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class EditRecordViewModel(macro: Macro) : ViewModel() {
    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }

    val macro: LiveData<Macro> = _macro

    fun pushEvent(event: MacroEvent) {
        macro.value!!.events.add(event)
    }

    fun resetEvents() {
        macro.value!!.events = arrayListOf()
    }
}
