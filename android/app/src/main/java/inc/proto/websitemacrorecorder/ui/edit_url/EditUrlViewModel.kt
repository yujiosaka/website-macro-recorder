package inc.proto.websitemacrorecorder.ui.edit_url

import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro

class EditUrlViewModel(macro: Macro) : ViewModel() {
    private var _macro = macro
}