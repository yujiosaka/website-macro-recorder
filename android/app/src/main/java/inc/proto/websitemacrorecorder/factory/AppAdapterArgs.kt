package inc.proto.websitemacrorecorder.factory

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsAdapter
import inc.proto.websitemacrorecorder.ui.list.ListAdapter
import inc.proto.websitemacrorecorder.ui.show_histories.ShowHistoriesAdapter

sealed class AppAdapterArgs

data class ListAdapterArgs(
    val options: FirestoreRecyclerOptions<Macro>,
    val listener: ListAdapter.Listener
): AppAdapterArgs()

data class EditEventsAdapterArgs(
    val events: ArrayList<MacroEvent>,
    val listener: EditEventsAdapter.Listener
): AppAdapterArgs()

data class ViewHistoriesAdapterArgs(
    val histories: ArrayList<MacroHistory>,
    val listener: ShowHistoriesAdapter.Listener
): AppAdapterArgs()
