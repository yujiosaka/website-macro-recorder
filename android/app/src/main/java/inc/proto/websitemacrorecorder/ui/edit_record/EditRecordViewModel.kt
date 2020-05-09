package inc.proto.websitemacrorecorder.ui.edit_record

import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro

class EditRecordViewModel(macro: Macro) : ViewModel() {
    private var _macro = macro
}