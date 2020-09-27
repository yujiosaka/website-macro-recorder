package inc.proto.websitemacrorecorder.ui.edit_events

import android.annotation.SuppressLint
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
    private var applicationContext: Context,
    private val events: ArrayList<MacroEvent>,
    private val listener: Listener
) : RecyclerView.Adapter<EditEventsViewHolder>() {
    interface Listener {
        fun onStartDragging(viewHolder: RecyclerView.ViewHolder)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EditEventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        val viewHolder = EditEventsViewHolder(view)

        viewHolder.imageReorder.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                listener.onStartDragging(viewHolder)
            }
            true
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: EditEventsViewHolder, position: Int) {
        val event = events[position]
        setContents(holder, event)
        setVisibilities(holder, event)
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

    private fun setContents(holder: EditEventsViewHolder, event: MacroEvent) {
        when (event.name) {
            "click" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = applicationContext.resources.getString(R.string.text_name_click)
            }
            "select" -> {
                holder.imageName.setImageResource(R.drawable.ic_touch_app_white_24dp)
                holder.textName.text = applicationContext.resources.getString(R.string.text_name_select)
            }
            "type" -> {
                holder.imageName.setImageResource(R.drawable.ic_keyboard_white_24dp)
                holder.textName.text = applicationContext.resources.getString(R.string.text_name_type)
            }
            "page" -> {
                holder.imageName.setImageResource(R.drawable.ic_history_white_24dp)
                holder.textName.text = applicationContext.resources.getString(R.string.text_name_page)
            }
            "timer" -> {
                holder.imageName.setImageResource(R.drawable.ic_timer_white_24dp)
                holder.textName.text = applicationContext.resources.getString(R.string.text_name_timer)
            }
            else -> {
                throw IllegalArgumentException("Unknown event name! ${event.name}")
            }
        }

        when (event.name) {
            "timer" -> {
                holder.textTargetType.text = applicationContext.resources.getString(R.string.text_target_type_wait_for_timer)
                holder.textValue.text = applicationContext.resources.getString(R.string.text_value_seconds, event.value)
            }
            "page" -> {
                holder.textTargetType.text = applicationContext.resources.getString(R.string.text_target_type_wait_for_navigation)
                holder.textValue.text = event.value
            }
            else -> {
                holder.textTargetType.text = try {
                    applicationContext.resources.getString(
                        applicationContext.resources.getIdentifier(
                            "text_target_type_${event.targetType}",
                            "string",
                            applicationContext.packageName
                        )
                    )
                } catch (e: Resources.NotFoundException) {
                    applicationContext.resources.getString(R.string.text_target_type_text)
                }
                holder.textValue.text = if (event.value != "" && event.targetType == "password") {
                    event.value.replace(".".toRegex(), "*")
                } else if (event.value != "") {
                    event.value
                } else {
                    applicationContext.resources.getString(R.string.text_value_empty)
                }
            }
        }
        holder.textMessage.text = if (event.message != "") event.message else ""
    }

    private fun setVisibilities(holder: EditEventsViewHolder, event: MacroEvent) {
        holder.imageReorder.visibility = when (event.name) {
            "click", "select", "type", "page" -> View.INVISIBLE
            "timer" -> View.VISIBLE
            else -> throw IllegalArgumentException("Unknown event name! ${event.name}")
        }
        holder.imageWarning.visibility = if (event.message != "") View.VISIBLE else View.INVISIBLE
    }

    private fun itemAt(position: Int): MacroEvent {
        return events[position]
    }
}
