package inc.proto.websitemacrorecorder.ui.edit_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.data.Macro

@Suppress("UNCHECKED_CAST")
class EditScheduleViewModelFactory(private val macro: Macro): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return EditScheduleViewModel(macro) as T
    }
}
