package inc.proto.websitemacrorecorder.ui.edit_events

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
        val event = events[position]
        when (event.name) {
            "click" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_click)
                holder.imageReorder.visibility = View.INVISIBLE
            }
            "select" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_select)
                holder.imageReorder.visibility = View.INVISIBLE
            }
            "type" -> {
                holder.imageName.setImageResource(R.drawable.ic_keyboard_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_type)
                holder.imageReorder.visibility = View.INVISIBLE
            }
            "page" -> {
                holder.imageName.setImageResource(R.drawable.ic_history_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_page)
                holder.imageReorder.visibility = View.INVISIBLE
            }
            "timer" -> {
                holder.imageName.setImageResource(R.drawable.ic_timer_white_24dp)
                holder.textName.text = context.resources.getString(R.string.text_name_timer)
                holder.imageReorder.visibility = View.VISIBLE
            }
            else -> {
                throw IllegalArgumentException("Unknown event name! ${event.name}")
            }
        }
        when (event.name) {
            "timer" -> {
                holder.textTargetType.text = context.resources.getString(R.string.text_target_type_wait_for_timer)
                holder.textValue.text = context.resources.getString(R.string.text_value_seconds, event.value)
            }
            "page" -> {
                holder.textTargetType.text = context.resources.getString(R.string.text_target_type_wait_for_navigation)
                holder.textValue.text = event.value
            }
            else -> {
                holder.textTargetType.text = try {
                    context.resources.getString(
                        context.resources.getIdentifier(
                            "text_target_type_${event.targetType}",
                            "string",
                            context.packageName
                        )
                    )
                } catch (e: Resources.NotFoundException) {
                    context.resources.getString(R.string.text_target_type_text)
                }
                holder.textValue.text = if (event.value != "" && event.targetType == "password") {
                    event.value.replace(".".toRegex(), "*")
                } else if (event.value != "") {
                    event.value
                } else {
                    context.resources.getString(R.string.text_value_empty)
                }
            }
        }
        if (event.message != "") {
            holder.textMessage.text = event.message
            holder.imageWarning.visibility = View.VISIBLE
        } else {
            holder.textMessage.text = ""
            holder.imageWarning.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount() = events.size

    fun moveItem(from: Int, to: Int) {
        Collections.swap(events, from, to)
        notifyItemMoved(from, to)
    }

    fun mergeItem(position: Int) {
        if (position == 0) return
        val lastEvent = itemAt(position - 1)
        val event = itemAt(position)
        if (lastEvent.name != "timer" || event.name != "timer") return
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
    }

    fun addItem(event: MacroEvent) {
        if (itemCount >= 1) {
            val lastEvent = itemAt(itemCount - 1)
            if (lastEvent.name == "timer" && event.name == "timer") {
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
    }

    fun setMessage(position: Int, message: String) {
        for (event in events) {
            event.message = ""
        }
        itemAt(position).message = message
        notifyDataSetChanged()
    }

    private fun itemAt(position: Int): MacroEvent {
        return events[position]
    }
}
