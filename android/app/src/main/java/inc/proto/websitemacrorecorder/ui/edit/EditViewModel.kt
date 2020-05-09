package inc.proto.websitemacrorecorder.ui.edit

import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro
}