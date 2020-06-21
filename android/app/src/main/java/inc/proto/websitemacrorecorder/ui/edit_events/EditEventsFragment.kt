package inc.proto.websitemacrorecorder.ui.edit_events

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.FirebaseFunctionsException
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.ui.dialog.edit_events_dialog.EditEventsDialog
import inc.proto.websitemacrorecorder.util.Helper.objectToMap
import kotlinx.android.synthetic.main.fragment_edit_events.*

class EditEventsFragment : Fragment(), EditEventsDialog.Listener {
    private val itemTouchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(
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
                        viewHolder!!.itemView.alpha = 0.5f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0f
                    adapter.mergeItem(viewHolder.adapterPosition)
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
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val adapter = recycler_events.adapter as EditEventsAdapter
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
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    if (context == null) return
                    drawBackground(viewHolder.itemView, dX).draw(c)
                    drawIcon(viewHolder.itemView).draw(c)
                }


                private fun drawBackground(view: View, dX: Float): Drawable {
                    val color = ContextCompat.getColor(requireContext(), R.color.colorDelete)
                    val background = ColorDrawable(color)
                    background.setBounds(view.left, view.top,view.left + dX.toInt(), view.bottom)
                    return background
                }

                private fun drawIcon(view: View): Drawable {
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
    }

    private val adapter: EditEventsAdapter by lazy {
        EditEventsAdapter(this, args.macro.events)
    }
    private val args: EditEventsFragmentArgs by navArgs()
    private val macroRepository = MacroRepository()
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_edit_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_events.setHasFixedSize(true)
        recycler_events.layoutManager = LinearLayoutManager(requireActivity())
        recycler_events.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recycler_events)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_events, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_timer -> {
                val dialog = EditEventsDialog()
                dialog.setListener(this)
                dialog.show(childFragmentManager, "edit_events_dialog")
                true
            }
            R.id.action_done -> {
                if (loading || activity == null) return false
                loading = true
                progress.visibility = View.VISIBLE
                itemTouchHelper.attachToRecyclerView(null)
                val macro = objectToMap(args.macro)
                macroRepository.screenshot(macro).addOnCompleteListener(requireActivity()) {
                    itemTouchHelper.attachToRecyclerView(recycler_events)
                    progress.visibility = View.GONE
                    loading = false
                    if (!it.isSuccessful) {
                        if (activity == null) return@addOnCompleteListener
                        val exception = it.exception as FirebaseFunctionsException
                        val root: View = requireActivity().findViewById(R.id.root)
                        val text = when (exception.code) {
                            FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> root.resources.getString(R.string.error_deadline_exceeded)
                            FirebaseFunctionsException.Code.UNAUTHENTICATED -> root.resources.getString(R.string.error_unauthenticated)
                            else -> root.resources.getString(R.string.error_unknown)
                        }
                        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    args.macro.screenshotUrl = it.result!!.data as String
                    val action = EditEventsFragmentDirections.actionEditEventsFragmentToConfirmFragment(args.macro)
                    findNavController().navigate(action)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onAddTimer(value: String) {
        val event = MacroEvent(name = "wait", value = value)
        adapter.addItem(event)
    }
}
