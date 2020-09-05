package inc.proto.websitemacrorecorder.ui.edit

import android.graphics.Paint
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.App
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class EditViewModel(macro: Macro) : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("paint")
        fun paintText(view: TextView, flag: Int?) {
            if (flag == null) return
            view.paintFlags = view.paintFlags or flag
        }
    }

    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }
    val macro: LiveData<Macro> = _macro

    val name: LiveData<String> = Transformations.map(_macro) {
        it.name
    }

    val url: LiveData<String> = Transformations.map(_macro) {
        it.url
    }

    val enableSchedule: LiveData<Boolean> = Transformations.map(_macro) {
        it.enableSchedule
    }

    val notifySuccess: LiveData<Boolean> = Transformations.map(_macro) {
        it.notifySuccess
    }

    val notifyFailure: LiveData<Boolean> = Transformations.map(_macro) {
        it.notifyFailure
    }

    val checkEntirePage: LiveData<Boolean> = Transformations.map(_macro) {
        it.checkEntirePage
    }

    val checkSelectedArea: LiveData<Boolean> = Transformations.map(_macro) {
        it.checkSelectedArea
    }

    val showSchedule: LiveData<Boolean> = Transformations.map(_macro) {
        it.scheduleFrequency == 1
    }

    val schedule: LiveData<String> = Transformations.map(_macro) {
        val frequencies = App.context.resources.getStringArray(R.array.text_frequency_array)
        if (it.scheduleFrequency == 1) {
            val schedule = when {
                it.scheduleEveryday -> App.context.resources.getString(R.string.text_schedule_everyday)
                it.scheduleWeekdays -> App.context.resources.getString(R.string.text_schedule_weekdays)
                it.scheduleWeekends -> App.context.resources.getString(R.string.text_schedule_weekends)
                it.scheduleDays == 1 && it.scheduleSunday -> App.context.resources.getString(R.string.text_schedule_sunday)
                it.scheduleDays == 1 && it.scheduleMonday -> App.context.resources.getString(R.string.text_schedule_monday)
                it.scheduleDays == 1 && it.scheduleTuesday -> App.context.resources.getString(R.string.text_schedule_tuesday)
                it.scheduleDays == 1 && it.scheduleWednesday -> App.context.resources.getString(R.string.text_schedule_wednesday)
                it.scheduleDays == 1 && it.scheduleThursday -> App.context.resources.getString(R.string.text_schedule_thursday)
                it.scheduleDays == 1 && it.scheduleFriday -> App.context.resources.getString(R.string.text_schedule_friday)
                it.scheduleDays == 1 && it.scheduleSaturday -> App.context.resources.getString(R.string.text_schedule_saturday)
                else -> App.context.resources.getString(R.string.text_schedule_days, it.scheduleDays.toString())
            }
            App.context.resources.getString(R.string.text_schedule, schedule, it.schedule)
        } else {
            frequencies[it.scheduleFrequency]
        }
    }

    val selectedArea: LiveData<String> = Transformations.map(_macro) {
        if (_macro.value!!.isAreaSelected) {
            App.context.resources.getString(
                R.string.text_check_selected_area,
                _macro.value!!.selectedAreaSize
            )
        } else {
            App.context.resources.getString(R.string.text_area_not_selected)
        }
    }

    val isAreaSelected: LiveData<Boolean> = Transformations.map(_macro) {
        _macro.value!!.isAreaSelected
    }

    fun validateName(editText: EditText) {
        if (_macro.value?.name == "") {
            editText.error = App.context.resources.getString(
                R.string.error_enter_here,
                App.context.resources.getString(R.string.name)
            )
        } else {
            editText.error = null
        }
    }

    fun resetMacro(macro: Macro) {
        _macro.value = macro
    }
}
