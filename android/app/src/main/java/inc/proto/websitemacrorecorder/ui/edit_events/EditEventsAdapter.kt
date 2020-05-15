package inc.proto.websitemacrorecorder.ui.edit_events

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Event
import java.util.*
import kotlin.collections.ArrayList

class EditEventsAdapter(
    private val fragment: EditEventsFragment,
    private val _events: ArrayList<Event>
) : RecyclerView.Adapter<EditEventsViewHolder>() {

    private lateinit var _context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EditEventsViewHolder {
        _context = parent.context

        val view = LayoutInflater.from(_context).inflate(R.layout.recycler_event, parent, false)
        val viewHolder = EditEventsViewHolder(view)

        viewHolder.imageReorder.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                fragment.startDragging(viewHolder)
            }
            return@setOnTouchListener true
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: EditEventsViewHolder, position: Int) {
        when (_events[position].name) {
            "click" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = _context.resources.getString(R.string.text_name_click)
            }
            "select" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = _context.resources.getString(R.string.text_name_select)
            }
            "change" -> {
                holder.imageName.setImageResource(R.drawable.ic_keyboard_white_24dp)
                holder.textName.text = _context.resources.getString(R.string.text_name_change)
            }
            "wait" -> {
                holder.imageName.setImageResource(R.drawable.ic_timer_white_24dp)
                holder.textName.text = _context.resources.getString(R.string.text_name_wait)
            }
        }

        if (_events[position].name == "wait") {
            holder.textTargetType.text = _context.resources.getString(R.string.text_target_type_timer)
            holder.textValue.text = _context.resources.getString(R.string.text_value_seconds, _events[position].value)
        } else {
            holder.textTargetType.text = try {
                _context.resources.getString(
                    _context.resources.getIdentifier(
                        "text_target_type_${_events[position].targetType}",
                        "string",
                        _context.packageName
                    )
                )
            } catch (e: Resources.NotFoundException) {
                _context.resources.getString(R.string.text_target_type_text)
            }
            holder.textValue.text = if (_events[position].value != "") {
                _events[position].value
            } else {
                _context.resources.getString(R.string.text_value_empty)
            }
        }
    }

    override fun getItemCount() = _events.size

    fun itemAt(position: Int): Event {
        return _events[position]
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(_events, from, to);
    }

    fun removeItem(position: Int) {
        _events.removeAt(position)
    }

    fun addItem(event: Event) {
        _events.add(event)
    }
}
