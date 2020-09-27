package inc.proto.websitemacrorecorder.ui.edit_url

import android.content.Context
import android.widget.EditText
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent
import org.apache.commons.validator.routines.UrlValidator

class EditUrlViewModel @ViewModelInject constructor(
    private val urlValidator: UrlValidator,
    @ApplicationContext private val applicationContext: Context,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    val isValid: LiveData<Boolean> = Transformations.map(_macro) {
        urlValidator.isValid(it.url)
    }

    fun validateUrl(editText: EditText) {
        if (!urlValidator.isValid(_macro.value?.url)) {
            editText.error = applicationContext.resources.getString(
                R.string.error_format_is_invalid,
                applicationContext.resources.getString(R.string.url)
            )
        } else {
            editText.error = null
        }
    }
}
