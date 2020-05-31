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
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.ui.edit_events_dialog.EditEventsDialog
import kotlinx.android.synthetic.main.fragment_edit_events.*

class EditEventsFragment : Fragment(), EditEventsDialog.Listener {

    private var _loading = false
    private val _gson = Gson()
    private val _args: EditEventsFragmentArgs by navArgs()

    private lateinit var _adapter: EditEventsAdapter
    private lateinit var _itemTouchHelper: ItemTouchHelper
    private lateinit var _screenshot: HttpsCallableReference

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

        _adapter = EditEventsAdapter(this, _args.macro.events)

        recycler_events.setHasFixedSize(true)
        recycler_events.layoutManager = LinearLayoutManager(requireActivity())
        recycler_events.adapter = _adapter

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
                        viewHolder!!.itemView.alpha = 0.5f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder.itemView.alpha = 1.0f
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
                    val color = ContextCompat.getColor(requireContext(), R.color.colorDelete)
                    val background = ColorDrawable(color)

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
        _screenshot = FirebaseFunctions.getInstance().getHttpsCallable("screenshot")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_events, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (_loading) return false
        return when (item.itemId) {
            R.id.action_add_timer -> {
                val dialog = EditEventsDialog()
                dialog.setListener(this)
                dialog.show(requireFragmentManager(), "edit_events_dialog")
                true
            }
            R.id.action_done -> {
                val json = _gson.toJson(_args.macro)
                val map: Map<String, Any> =_gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
                _startLoading()
                _screenshot.call(map).addOnCompleteListener { task->
                    _finishLoading()
                    if (!task.isSuccessful) {
                        val exception = task.exception as FirebaseFunctionsException
                        val root: View = requireActivity().findViewById(R.id.root)
                        val text = when (exception.code) {
                            FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> root.resources.getString(R.string.error_deadline_exceeded)
                            FirebaseFunctionsException.Code.UNAUTHENTICATED -> root.resources.getString(R.string.error_unauthenticated)
                            else -> root.resources.getString(R.string.error_unknown)
                        }
                        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    _args.macro.screenshotUrl = task.result!!.data as String
                    val action = EditEventsFragmentDirections.actionEditEventsFragmentToConfirmFragment(_args.macro)
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
        _itemTouchHelper.startDrag(viewHolder)
    }

    override fun onAddTimer(value: String) {
        val event = MacroEvent(name = "wait", value = value)
        if (_adapter.itemCount >= 1) {
            val lastEvent = _adapter.itemAt(_adapter.itemCount - 1)
            if (lastEvent.name == "wait") {
                val lastValue = Integer.parseInt(lastEvent.value)
                val value = Integer.parseInt(event.value)
                var totalValue = lastValue + value
                if (totalValue >= MacroEvent.MAX_WAIT_VALUE) {
                    totalValue = MacroEvent.MAX_WAIT_VALUE
                }
                lastEvent.value = totalValue.toString()
                _adapter.notifyItemChanged(_adapter.itemCount - 1, lastEvent)
                return
            }
        }
        _adapter.addItem(event)
        _adapter.notifyItemInserted(_adapter.itemCount)
    }

    fun _startLoading() {
        _loading = true
        filter.visibility = View.VISIBLE
        loading.visibility = View.VISIBLE
        _itemTouchHelper.attachToRecyclerView(null)
    }

    fun _finishLoading() {
        _itemTouchHelper.attachToRecyclerView(recycler_events)
        loading.visibility = View.GONE
        filter.visibility = View.GONE
        _loading = false
    }
}
