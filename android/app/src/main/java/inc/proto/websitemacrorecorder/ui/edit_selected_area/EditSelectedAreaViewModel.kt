package inc.proto.websitemacrorecorder.ui.edit_selected_area

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent

class EditSelectedAreaViewModel @ViewModelInject constructor(
    private val macroRepository: MacroRepository,
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

    fun updateMacro(update: Map<String, Any?>) {
        macroRepository.update(_macro.value!!.id, update)
    }
}
