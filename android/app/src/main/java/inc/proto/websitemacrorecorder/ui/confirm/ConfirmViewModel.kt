package inc.proto.websitemacrorecorder.ui.confirm

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.HttpsCallableResult
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent

class ConfirmViewModel @ViewModelInject constructor(
    private val macroRepository: MacroRepository,
    private val helper: Helper,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("image")
        fun loadImage(view: ImageView, url: String?) {
            if (url == null) return
            Glide.with(view.context).load(url).into(view)
        }
    }

    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    fun createMacro(): Task<HttpsCallableResult> {
        return macroRepository.create(helper.objectToMap(_macro.value!!))
    }
}
