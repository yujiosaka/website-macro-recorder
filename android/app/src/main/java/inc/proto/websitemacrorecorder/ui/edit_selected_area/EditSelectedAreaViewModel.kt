package inc.proto.websitemacrorecorder.ui.edit_selected_area

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class EditSelectedAreaViewModel(macro: Macro) : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("image")
        fun loadImage(view: ImageView, url: String?) {
            if (url == null) return
            Glide.with(view.context).load(url).into(view)
        }
    }

    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }

    val macro: LiveData<Macro> = _macro

    val screenshotUrl: LiveData<String> = Transformations.map(_macro) {
        it.screenshotUrl
    }
}
