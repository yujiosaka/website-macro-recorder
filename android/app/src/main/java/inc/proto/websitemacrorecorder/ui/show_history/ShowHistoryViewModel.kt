package inc.proto.websitemacrorecorder.ui.show_history

import android.graphics.Rect
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.ui.view.DragRectView
import inc.proto.websitemacrorecorder.util.ObservableSingleLiveEvent
import inc.proto.websitemacrorecorder.util.TextFormatter

class ShowHistoryViewModel @ViewModelInject constructor(
    private val textFormatter: TextFormatter,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        @JvmStatic
        @BindingAdapter("image")
        fun loadImage(view: ImageView, url: String?) {
            if (url == null) return
            Glide.with(view.context).load(url).into(view)
        }

        @JvmStatic
        @BindingAdapter("rect")
        fun setRect(view: DragRectView, rect: Rect?) {
            if (rect == null) return
            view.setRect(rect)
        }

        @JvmStatic
        @BindingAdapter("enabled")
        fun setEnabled(view: DragRectView, isEnabled: Boolean?) {
            if (isEnabled == null) return
            view.isEnabled = isEnabled
        }
    }

    private val _macro = ObservableSingleLiveEvent<Macro>().also {
        it.value = savedStateHandle.get("macro")
    }
    val macro: LiveData<Macro> = _macro

    private val _history = ObservableSingleLiveEvent<MacroHistory>().also {
        it.value = savedStateHandle.get("history")
    }
    val history: LiveData<MacroHistory> = _history

    private val _isHistory = MutableLiveData<Boolean>().also {
        it.value = true
    }
    val isHistory: LiveData<Boolean> = _isHistory

    val screenshotUrl: LiveData<String> = Transformations.map(_isHistory) {
        if (it) {
            _history.value!!.screenshotUrl
        } else {
            _macro.value!!.screenshotUrl
        }
    }

    val macroDate: LiveData<String> = Transformations.map(_macro) {
        textFormatter.dateToDateTime(it.createdAt!!.toDate())
    }

    val historyDate: LiveData<String> = Transformations.map(_history) {
        textFormatter.dateToDateTime(it.executedAt!!.toDate())
    }

    val selectedAreaRect: LiveData<Rect> = Transformations.map(_macro) {
        it.getSelectedAreaRect()
    }

    fun clickMacro() {
        _isHistory.value = false
    }

    fun clickHistory() {
        _isHistory.value = true
    }
}
