package inc.proto.websitemacrorecorder.ui.edit_schedule

import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditScheduleViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro
}
