package inc.proto.websitemacrorecorder.ui.edit

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.App
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class EditViewModel(macro: Macro) : ViewModel() {
    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }
    val macro: LiveData<Macro> = _macro

    val showSchedule: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleFrequency == 1
    }

    val schedule: LiveData<String> = Transformations.map(_macro) {
        val frequencies = App.context.resources.getStringArray(R.array.text_frequency_array)
        val schedule = frequencies[it.scheduleFrequency]
        if (it.scheduleFrequency == 1) {
            App.context.resources.getString(R.string.text_schedule, schedule, it.schedule)
        } else {
            schedule
        }
    }

    fun validateName(editText: EditText) {
        if (_macro.value?.name == "") {
            editText.error = editText.resources.getString(
                R.string.error_enter_here,
                editText.resources.getString(R.string.name)
            )
        } else {
            editText.error = null
        }
    }

    fun resetMacro(macro: Macro) {
        _macro.value = macro
    }
}
