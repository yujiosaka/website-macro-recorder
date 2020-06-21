package inc.proto.websitemacrorecorder.ui.edit_events

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import java.util.*
import kotlin.collections.ArrayList

class EditEventsAdapter(
    private val fragment: EditEventsFragment,
    private val events: ArrayList<MacroEvent>
) : RecyclerView.Adapter<EditEventsViewHolder>() {
    private lateinit var context: Context

    init {
        switchTips()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EditEventsViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
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
        when (events[position].name) {
            "click" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_click)
            }
            "select" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_select)
            }
            "type" -> {
                holder.imageName.setImageResource(R.drawable.ic_keyboard_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_type)
            }
            "wait" -> {
                holder.imageName.setImageResource(R.drawable.ic_timer_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_wait)
            }
        }

        if (events[position].name == "wait") {
            holder.textTargetType.text = context.resources.getString(R.string.text_target_type_timer)
            holder.textValue.text = context.resources.getString(R.string.text_value_seconds, events[position].value)
        } else {
            holder.textTargetType.text = try {
                context.resources.getString(
                    context.resources.getIdentifier(
                        "text_target_type_${events[position].targetType}",
                        "string",
                        context.packageName
                    )
                )
            } catch (e: Resources.NotFoundException) {
                context.resources.getString(R.string.text_target_type_text)
            }
            holder.textValue.text = if (events[position].value != "") {
                events[position].value
            } else {
                context.resources.getString(R.string.text_value_empty)
            }
        }
    }

    override fun getItemCount() = events.size

    fun itemAt(position: Int): MacroEvent {
        return events[position]
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(events, from, to)
        notifyItemMoved(from, to)
    }

    fun mergeItem(position: Int) {
        if (position == 0) return
        val lastEvent = itemAt(position - 1)
        val event = itemAt(position)
        if (lastEvent.name != "wait" || event.name != "wait") return
        val lastValue = Integer.parseInt(lastEvent.value)
        var totalValue = lastValue + Integer.parseInt(event.value)
        if (totalValue >= MacroEvent.MAX_WAIT_VALUE) {
            totalValue = MacroEvent.MAX_WAIT_VALUE
        }
        lastEvent.value = totalValue.toString()
        notifyItemChanged(position - 1, lastEvent)
        events.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItem(position: Int) {
        events.removeAt(position)
        notifyItemRemoved(position)
        switchTips()
    }

    fun addItem(event: MacroEvent) {
        if (itemCount >= 1) {
            val lastEvent = itemAt(itemCount - 1)
            if (lastEvent.name == "wait" && event.name == "wait") {
                val lastValue = Integer.parseInt(lastEvent.value)
                var totalValue = lastValue + Integer.parseInt(event.value)
                if (totalValue >= MacroEvent.MAX_WAIT_VALUE) {
                    totalValue = MacroEvent.MAX_WAIT_VALUE
                }
                lastEvent.value = totalValue.toString()
                notifyItemChanged(itemCount - 1, lastEvent)
                return
            }
        }
        events.add(event)
        notifyItemInserted(itemCount)
        switchTips()
    }

    private fun switchTips() {
        if (fragment.view == null) return
        fragment.requireView().findViewById<TextView>(R.id.text_tips).visibility = if (itemCount == 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
