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

    val scheduleFrequency: LiveData<Int> = Transformations.map(_macro) {
        it.scheduleFrequency
    }

    val schedule: LiveData<String> = Transformations.map(_macro) {
        it.schedule
    }

    val scheduleSunday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleSunday
    }

    val scheduleMonday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleMonday
    }

    val scheduleTuesday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleTuesday
    }

    val scheduleWednesday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleWednesday
    }

    val scheduleThursday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleThursday
    }

    val scheduleFriday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleFriday
    }

    val scheduleSaturday: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleSaturday
    }
}
