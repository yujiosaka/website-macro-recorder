package inc.proto.websitemacrorecorder.ui.edit_url

import android.widget.EditText
import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.R
import org.apache.commons.validator.routines.UrlValidator

class EditUrlViewModel(macro: Macro) : ObservableViewModel() {

    private var _macro = macro
    private var _valid = false

    @get:Bindable
    var macro
        get() = _macro
        set(value) {
            _macro = value
            notifyPropertyChanged(BR.macro)
        }

    @get:Bindable
    var url
        get() = _macro.url
        set(value) {
            _macro.url = value
            notifyPropertyChanged(BR.url)
        }

    @get:Bindable
    var valid
        get() = _valid
        set(value) {
            _valid = value
            notifyPropertyChanged(BR.valid)
        }

    fun validate(editText: EditText) {
        if (_macro.url == "") {
            editText.error = editText.resources.getString(
                R.string.error_enter_here,
                editText.resources.getString(R.string.url)
            )
            _valid = false
        } else if (!UrlValidator.getInstance().isValid(_macro.url)) {
            editText.error = editText.resources.getString(
                R.string.error_format_is_invalid,
                editText.resources.getString(R.string.url)
            )
            _valid = false
        } else {
            editText.error = null
            _valid = true
        }
        notifyPropertyChanged(BR.valid)
    }
}
