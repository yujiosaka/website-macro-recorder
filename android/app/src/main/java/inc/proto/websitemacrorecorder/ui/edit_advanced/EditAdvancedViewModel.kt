package inc.proto.websitemacrorecorder.ui.edit_advanced

import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditAdvancedViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro
}