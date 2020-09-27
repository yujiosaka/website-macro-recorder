package inc.proto.websitemacrorecorder.ui.edit_events

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.HttpsCallableResult
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent

class EditEventsViewModel @ViewModelInject constructor(
    private val macroRepository: MacroRepository,
    private val helper: Helper,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    fun screenshot(): Task<HttpsCallableResult> {
        return macroRepository.screenshot(helper.objectToMap(macro.value!!))
    }
}
