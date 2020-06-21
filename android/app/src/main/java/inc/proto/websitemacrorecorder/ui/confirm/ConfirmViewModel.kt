package inc.proto.websitemacrorecorder.ui.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class ConfirmViewModel(macro: Macro) : ViewModel() {
    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }
    val macro: LiveData<Macro> = _macro
}
