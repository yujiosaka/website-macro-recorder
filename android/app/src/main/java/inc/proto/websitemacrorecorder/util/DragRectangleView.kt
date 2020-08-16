package inc.proto.websitemacrorecorder.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import inc.proto.websitemacrorecorder.R
import kotlin.math.abs


class DragRectView : View {
    companion object {
        private const val MIN_RECT_LENGTH = 5
        private const val STROKE_WIDTH = 10f
    }

    interface Listener {
        fun startDraw()
        fun endDraw(rect: Rect)
    }

    private var paint: Paint? = null
    private var startX = 0
    private var startY = 0
    private var endX = 0
    private var endY = 0
    private var pressing = false
    private var drawing = false
    private lateinit var listener: Listener

    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            startX = e.x.toInt()
            startY = e.y.toInt()
            invalidate()
            pressing = true
            drawing = false
            listener.startDraw()
        }
    })

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun setListener(_listener: Listener) {
        listener = _listener
    }

    fun setRect(rect: Rect) {
        startX = rect.left
        startY = rect.top
        endX = rect.right
        endY = rect.bottom
        drawing = true
        invalidate()
    }

    fun resetDraw() {
        startX = 0
        startY = 0
        endX = 0
        endY = 0
        drawing = false
        invalidate()
    }

    private fun init() {
        paint = Paint()
        paint!!.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeWidth = STROKE_WIDTH
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!pressing) return true
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (!drawing || abs(x - endX) > MIN_RECT_LENGTH || abs(y - endY) > MIN_RECT_LENGTH) {
                    endX = x
                    endY = y
                    invalidate()
                }
                drawing = true
            }
            MotionEvent.ACTION_UP -> {
                if (!pressing) return true
                listener?.endDraw(Rect(
                    startX.coerceAtMost(endX),
                    startY.coerceAtMost(endY),
                    endX.coerceAtLeast(startX),
                    startY.coerceAtLeast(endY)
                ))
                invalidate()
                pressing = false
            }
            else -> {
                gestureDetector.onTouchEvent(event)
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!drawing) return
        canvas.drawRect(
            startX.coerceAtMost(endX).toFloat(),
            startY.coerceAtMost(endY).toFloat(),
            endX.coerceAtLeast(startX).toFloat(),
            endY.coerceAtLeast(startY).toFloat(),
            paint!!
        )
    }
}
