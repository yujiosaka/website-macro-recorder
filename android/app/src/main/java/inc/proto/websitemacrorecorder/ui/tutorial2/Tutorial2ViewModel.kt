package inc.proto.websitemacrorecorder.ui.tutorial2

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class Tutorial2ViewModel : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("image")
        fun loadImage(view: ImageView, id: Int?) {
            if (id == null) return
            Glide.with(view.context).load(id).into(view)
        }
    }
}
