package inc.proto.websitemacrorecorder.ui.edit_record

import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditRecordViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro
}