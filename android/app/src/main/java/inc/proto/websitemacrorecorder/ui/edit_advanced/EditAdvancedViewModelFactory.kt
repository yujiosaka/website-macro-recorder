package inc.proto.websitemacrorecorder.ui.edit_advanced

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro

class EditAdvancedViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return EditAdvancedViewModel(macro) as T
    }
}