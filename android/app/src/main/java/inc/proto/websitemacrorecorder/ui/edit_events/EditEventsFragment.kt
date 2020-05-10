package inc.proto.websitemacrorecorder.ui.edit_events

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import inc.proto.websitemacrorecorder.R
import kotlinx.android.synthetic.main.fragment_edit_events.*

class EditEventsFragment : Fragment() {

    private val _args: EditEventsFragmentArgs by navArgs()

    private lateinit var _itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         recycler_events.setHasFixedSize(true)
        recycler_events.layoutManager = LinearLayoutManager(activity)
        recycler_events.adapter = EditEventsAdapter(this, _args.macro.events)

        _itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT) {

                override fun isItemViewSwipeEnabled(): Boolean {
                    return true
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ACTION_STATE_DRAG) {
                        viewHolder!!.itemView!!.alpha = 0.5f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder!!.itemView!!.alpha = 1.0f
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as EditEventsAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    adapter.moveItem(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val adapter = recycler_events.adapter as EditEventsAdapter

                    adapter.removeItem(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
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
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                    _drawBackground(viewHolder.itemView, dX).draw(c)
                    _drawIcon(viewHolder.itemView).draw(c)
                }

                private fun _drawBackground(view: View, dX: Float): Drawable {
                    val background = ColorDrawable(resources.getColor(R.color.colorDelete))

                    background.setBounds(view.left, view.top,view.left + dX.toInt(), view.bottom)

                    return background
                }

                private fun _drawIcon(view: View): Drawable {
                    val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_white_24dp)!!

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
        )
        _itemTouchHelper.attachToRecyclerView(recycler_events)
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        _itemTouchHelper.startDrag(viewHolder)
    }
}
