package inc.proto.websitemacrorecorder.ui.view_history

import android.graphics.Rect
import android.text.format.DateFormat
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import inc.proto.websitemacrorecorder.App
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.util.DragRectView
import inc.proto.websitemacrorecorder.util.ObservableMutableLiveData

class ViewHistoryViewModel(macro: Macro, history: MacroHistory) : ViewModel() {
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

    private val _isHistory = MutableLiveData<Boolean>().also {
        it.value = true
    }

    private val _macro = ObservableMutableLiveData<Macro>().also {
        it.value = macro
    }

    private val _history = ObservableMutableLiveData<MacroHistory>().also {
        it.value = history
    }

    val macro: LiveData<Macro> = _macro

    val history: LiveData<MacroHistory> = _history

    val isHistory: LiveData<Boolean> = _isHistory

    val screenshotUrl: LiveData<String> = Transformations.map(_isHistory) {
        if (it) {
            _history.value!!.screenshotUrl
        } else {
            _macro.value!!.screenshotUrl
        }
    }

    val macroDate: LiveData<String> = Transformations.map(_macro) {
        formatDate(it.createdAt!!)
    }

    val historyDate: LiveData<String> = Transformations.map(_history) {
        formatDate(it.executedAt!!)
    }

    val selectedAreaRect: LiveData<Rect> = Transformations.map(_macro) {
        it.selectedAreaRect
    }

    fun clickMacro() {
        _isHistory.value = false
    }

    fun clickHistory() {
        _isHistory.value = true
    }

    private fun formatDate(executedAt: Timestamp): String {
        val date = DateFormat.getMediumDateFormat(App.context).format(executedAt.toDate())
        val time = DateFormat.getTimeFormat(App.context).format(executedAt.toDate())
        return "$date $time"
    }
}
