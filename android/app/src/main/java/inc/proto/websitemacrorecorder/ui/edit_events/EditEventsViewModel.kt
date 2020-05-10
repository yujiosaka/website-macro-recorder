package inc.proto.websitemacrorecorder.ui.edit_events

import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableViewModel

class EditEventsViewModel(macro: Macro) : ObservableViewModel() {
    private var _macro = macro
}
