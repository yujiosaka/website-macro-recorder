package inc.proto.websitemacrorecorder.ui.view_histories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.data.MacroHistory
import java.util.*
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
    }

    override fun getItemCount() = histories.size
}
