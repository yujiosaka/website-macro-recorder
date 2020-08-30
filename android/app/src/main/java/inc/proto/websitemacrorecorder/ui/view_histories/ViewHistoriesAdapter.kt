package inc.proto.websitemacrorecorder.ui.view_histories

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener
import kotlin.collections.ArrayList

class ViewHistoriesAdapter(private val histories: ArrayList<MacroHistory>) : RecyclerView.Adapter<ViewHistoriesViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHistoriesViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return ViewHistoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHistoriesViewHolder, position: Int) {
        val history = histories[position]
        Glide.with(context).load(history.screenshotUrl).into(holder.imageScreenshot)
        holder.textDate.text = if (history.executedAt != null) {
            val date = DateFormat.getMediumDateFormat(context).format(history.executedAt!!.toDate())
            val time = DateFormat.getTimeFormat(context).format(history.executedAt!!.toDate())
            "$date $time"
        } else {
            context.resources.getString(R.string.text_date_latest)
        }
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

    override fun getItemCount() = histories.size
}
