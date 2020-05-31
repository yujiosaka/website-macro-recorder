package inc.proto.websitemacrorecorder.ui.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro

@Suppress("UNCHECKED_CAST")
class ConfirmViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return ConfirmViewModel(macro) as T
    }
}
