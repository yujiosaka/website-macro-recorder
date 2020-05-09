package inc.proto.websitemacrorecorder.ui.edit_url

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro

class EditUrlViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return EditUrlViewModel(macro) as T
    }
}