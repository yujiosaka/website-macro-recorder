package inc.proto.websitemacrorecorder.util

import android.view.View

class OnSingleClickListener : View.OnClickListener {
    companion object {
        private const val DELAY_MILLIS = 200L
        private var previousClickTimeMillis = 0L
    }

    private val onClickListener: View.OnClickListener

    constructor(listener: View.OnClickListener) {
        onClickListener = listener
    }

    constructor(listener: (View) -> Unit) {
        onClickListener = View.OnClickListener { listener.invoke(it) }
    }

    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis < previousClickTimeMillis + DELAY_MILLIS) return
        previousClickTimeMillis = currentTimeMillis
        onClickListener.onClick(v)
    }
}
