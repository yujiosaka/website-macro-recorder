package inc.proto.websitemacrorecorder.ui.edit

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent
import inc.proto.websitemacrorecorder.util.TextFormatter

class EditViewModel @ViewModelInject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val macroRepository: MacroRepository,
    private val textFormatter: TextFormatter,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("paint")
        fun paintText(view: TextView, flag: Int?) {
            if (flag == null) return
            view.paintFlags = view.paintFlags or flag
        }
    }

    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    val schedule: LiveData<String> = Transformations.map(_macro) {
        textFormatter.macroToSchedule(it)
    }

    val selectedArea: LiveData<String> = Transformations.map(_macro) {
        if (_macro.value!!.isAreaSelected()) {
            applicationContext.resources.getString(
                R.string.text_check_selected_area,
                _macro.value!!.getSelectedAreaSize()
            )
        } else {
            applicationContext.resources.getString(R.string.text_area_not_selected)
        }
    }

    val isAreaSelected: LiveData<Boolean> = Transformations.map(_macro) {
        _macro.value!!.isAreaSelected()
    }

    fun validateName(editText: EditText) {
        if (_macro.value?.name == "") {
            editText.error = applicationContext.resources.getString(
                R.string.error_enter_here,
                applicationContext.resources.getString(R.string.name)
            )
        } else {
            editText.error = null
        }
    }

    fun deleteMacro() {
        macroRepository.delete(_macro.value!!.id)
    }

    fun updateMacro(update: Map<String, Any>) {
        macroRepository.update(_macro.value!!.id, update)
    }
}
