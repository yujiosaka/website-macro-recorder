package inc.proto.websitemacrorecorder.ui.edit_selected_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.ui.edit_url.EditUrlViewModel

@Suppress("UNCHECKED_CAST")
class EditSelectedAreaViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return EditSelectedAreaViewModel(macro) as T
    }
}
