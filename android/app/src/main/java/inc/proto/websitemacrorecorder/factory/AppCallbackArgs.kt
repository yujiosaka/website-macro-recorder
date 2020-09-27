package inc.proto.websitemacrorecorder.factory

import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsAdapter
import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsViewModel

sealed class AppCallbackArgs

data class EditEventsCallbackArgs(
    val dragDirs: Int,
    val swipeDirs: Int,
    val vm: EditEventsViewModel,
    val adapter: EditEventsAdapter
): AppCallbackArgs()
