package inc.proto.websitemacrorecorder.ui.edit_schedule

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent

class EditScheduleViewModel @ViewModelInject constructor(
    private val macroRepository: MacroRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    val scheduleTime: LiveData<String> = Transformations.map(_macro) {
        it.getScheduleTime()
    }

    fun updateMacro(update: Map<String, Any>) {
        macroRepository.update(_macro.value!!.id, update)
    }
}
