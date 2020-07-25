package inc.proto.websitemacrorecorder.ui.edit_url

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.App
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData
import org.apache.commons.validator.routines.UrlValidator

class EditUrlViewModel(macro: Macro) : ViewModel() {
    private val urlValidator = UrlValidator.getInstance()

    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }

    val macro: LiveData<Macro> = _macro

    val validUrl: LiveData<Boolean> = Transformations.map(_macro) {
        urlValidator.isValid(it.url)
    }

    fun validateUrl(editText: EditText) {
        if (!urlValidator.isValid(_macro.value?.url)) {
            editText.error = App.context.resources.getString(
                R.string.error_format_is_invalid,
                App.context.resources.getString(R.string.url)
            )
        } else {
            editText.error = null
        }
    }
}
