package inc.proto.websitemacrorecorder.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class TopCropImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        recomputeImgMatrix()
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        recomputeImgMatrix()
        return super.setFrame(l, t, r, b)
    }

    private fun init() {
        scaleType = ScaleType.MATRIX
    }

    private fun recomputeImgMatrix() {
        if (drawable == null) return
        val viewWidth = (width - paddingLeft - paddingRight).toFloat()
        val viewHeight = (height - paddingTop - paddingBottom).toFloat()
        val areaWidth = drawable.intrinsicWidth * viewHeight
        val areaHeight = drawable.intrinsicHeight * viewWidth
        val scale = if (areaWidth > areaHeight) {
            viewHeight / drawable.intrinsicHeight
        } else {
            viewWidth / drawable.intrinsicWidth
        }
        imageMatrix.setScale(scale, scale)
    }
}
