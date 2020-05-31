package inc.proto.websitemacrorecorder.ui.edit_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class EditScheduleViewModel(macro: Macro) : ViewModel() {
    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }

    val macro: LiveData<Macro> = _macro

    val showSchedule: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleFrequency == 1
    }
}
