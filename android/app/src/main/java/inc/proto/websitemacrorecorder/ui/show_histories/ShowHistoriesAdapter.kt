package inc.proto.websitemacrorecorder.ui.show_histories

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener
import kotlin.collections.ArrayList

class ShowHistoriesAdapter(
    private val applicationContext: Context,
    private val histories: ArrayList<MacroHistory>,
    private val listener: Listener
) : RecyclerView.Adapter<ShowHistoriesViewHolder>() {
    interface Listener {
        fun onViewHistory(history: MacroHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ShowHistoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ShowHistoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowHistoriesViewHolder, position: Int) {
        val history = histories[position]

        setContents(holder, history)
        setVisibilities(holder, history)
        setListeners(holder, history)
    }

    override fun getItemCount() = histories.size

    private fun setContents(holder: ShowHistoriesViewHolder, history: MacroHistory) {
        Glide.with(applicationContext).load(history.screenshotUrl).into(holder.imageScreenshot)

        holder.textDate.text = if (history.executedAt != null) {
            val date = DateFormat.getMediumDateFormat(applicationContext).format(history.executedAt!!.toDate())
            val time = DateFormat.getTimeFormat(applicationContext).format(history.executedAt!!.toDate())
            "$date $time"
        } else {
            applicationContext.resources.getString(R.string.text_date_latest)
        }
    }

    private fun setVisibilities(holder: ShowHistoriesViewHolder, history: MacroHistory) {
        holder.chipSuccess.visibility = if (history.isFailure) {
            View.GONE
        } else {
            View.VISIBLE
        }
        holder.chipError.visibility = if (history.isFailure) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.chipChange.visibility = if (history.isEntirePageUpdated || history.isSelectedAreaUpdated) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setListeners(holder: ShowHistoriesViewHolder, history: MacroHistory) {
        holder.imageScreenshot.setOnSingleClickListener {
            listener.onViewHistory(history)
        }
        holder.chipSuccess.setOnSingleClickListener {
            listener.onViewHistory(history)
        }
        holder.chipError.setOnSingleClickListener {
            listener.onViewHistory(history)
        }
        holder.chipChange.setOnSingleClickListener {
            listener.onViewHistory(history)
        }
        holder.chipCheckScreenshot.setOnSingleClickListener {
            listener.onViewHistory(history)
        }
    }
}
