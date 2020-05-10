package inc.proto.websitemacrorecorder.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro

class EditViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return EditViewModel(macro) as T
    }
}
