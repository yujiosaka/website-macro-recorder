package inc.proto.websitemacrorecorder.ui.edit

import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro

class EditViewModel(macro: Macro) : ViewModel() {
    private var _macro = macro
}