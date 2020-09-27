package inc.proto.websitemacrorecorder.ui.edit_events

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import inc.proto.websitemacrorecorder.R

class EditEventsCallback(
    dragDirs: Int,
    swipeDirs: Int,
    private val vm: EditEventsViewModel,
    private val adapter: EditEventsAdapter,
    private val applicationContext: Context
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun getDragDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val event = vm.macro.value!!.events[viewHolder.adapterPosition]
        return if (event.name == "timer") ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder!!.itemView.alpha = 0.5f
        }
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = 1.0f
        if (viewHolder.adapterPosition == -1) return

        adapter.mergeItem(viewHolder.adapterPosition)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.moveItem(from, to)

        return true
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        adapter.removeItem(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        drawBackground(viewHolder.itemView, dX).draw(c)
        drawIcon(viewHolder.itemView).draw(c)
    }


    private fun drawBackground(view: View, dX: Float): Drawable {
        val color = ContextCompat.getColor(applicationContext, R.color.colorDelete)
        val background = ColorDrawable(color)
        background.setBounds(view.left, view.top,view.left + dX.toInt(), view.bottom)
        return background
    }

    private fun drawIcon(view: View): Drawable {
        val icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_delete_white_24dp)!!
        val height = view.bottom - view.top
        val margin = (height - icon.intrinsicHeight) / 2
        val left = view.left + margin
        val top = view.top + (height - icon.intrinsicHeight) / 2
        val right = view.left + (margin + icon.intrinsicWidth)
        val bottom = top + icon.intrinsicHeight
        icon.setBounds(left, top, right, bottom)
        return icon
    }
}
