package inc.proto.websitemacrorecorder.ui.edit_url

import androidx.databinding.Bindable
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel
import inc.proto.websitemacrorecorder.BR

class EditUrlViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro

    @get:Bindable
    var url
        get() = _macro.url
        set(value) {
            _macro.url = value
            notifyPropertyChanged(BR.url)
        }

}