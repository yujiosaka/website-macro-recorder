package inc.proto.websitemacrorecorder.ui.view_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroHistory

@Suppress("UNCHECKED_CAST")
class ViewHistoryViewModelFactory(private val macro: Macro, private val history: MacroHistory): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return ViewHistoryViewModel(macro, history) as T
    }
}
