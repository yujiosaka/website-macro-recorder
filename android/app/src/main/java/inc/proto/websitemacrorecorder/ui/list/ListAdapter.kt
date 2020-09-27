package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener
import inc.proto.websitemacrorecorder.util.TextFormatter

class ListAdapter(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
    private val macroRepository: MacroRepository,
    private val textFormatter: TextFormatter,
    options: FirestoreRecyclerOptions<Macro>,
    private val listener: Listener
) : FirestoreRecyclerAdapter<Macro, ListViewHolder>(options) {
    interface Listener {
        fun onExecuteMacro(macro: Macro)
        fun onEditMacro(macro: Macro)
        fun onViewHistories(macro: Macro)
        fun onOpenWebsite(macro: Macro)
    }

    companion object {
        private const val ACTION_EDIT_MACRO = 1
        private const val ACTION_VIEW_HISTORIES = 2
        private const val ACTION_OPEN_WEBSITE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_macro, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Macro) {
        setContents(holder, model)
        setVisibilities(holder, model)
        setListeners(holder, model)
    }

    private fun setContents(holder: ListViewHolder, model: Macro) {
        Glide.with(applicationContext).load(model.screenshotUrl).into(holder.imageScreenshot)
        holder.textSchedule.text = textFormatter.macroToSchedule(model)
        holder.textName.text = if (model.name != "") {
            model.name
        } else {
            applicationContext.resources.getString(R.string.text_no_name)
        }
        holder.textUrl.text = model.url

        val order = sharedPreferences.getInt("ORDER", 0)
        holder.textDate.text = when (order) {
            Macro.ORDER_UPDATED_AT_DESC_VALUE, Macro.ORDER_UPDATED_AT_ASC_VALUE -> {
                if (model.updatedAt != null) {
                    DateFormat.getMediumDateFormat(applicationContext).format(model.updatedAt!!.toDate())
                } else {
                    applicationContext.resources.getString(R.string.text_date_latest)
                }
            }
            Macro.ORDER_CREATED_AT_DESC_VALUE, Macro.ORDER_CREATED_AT_ASC_VALUE -> {
                if (model.createdAt != null) {
                    DateFormat.getMediumDateFormat(applicationContext).format(model.createdAt!!.toDate())
                } else {
                    applicationContext.resources.getString(R.string.text_date_latest)
                }
            }
            else -> {
                if (model.executedAt != null) {
                    DateFormat.getMediumDateFormat(applicationContext).format(model.executedAt!!.toDate())
                } else {
                    applicationContext.resources.getString(R.string.text_date_latest)
                }
            }
        }
    }

    private fun setVisibilities(holder: ListViewHolder, model: Macro) {
        holder.editEnableSchedule.visibility = if (model.scheduleFrequency == 0) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        holder.chipError.visibility = if (model.isFailure) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.chipChange.visibility = if (model.isEntirePageUpdated || model.isSelectedAreaUpdated) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setListeners(holder: ListViewHolder, model: Macro) {
        holder.editEnableSchedule.setOnCheckedChangeListener(null)
        holder.editEnableSchedule.isChecked = model.enableSchedule
        holder.editEnableSchedule.setOnCheckedChangeListener { _, isChecked ->
            macroRepository.update(model.id, mapOf(
                "enableSchedule" to isChecked,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }
        holder.imageScreenshot.setOnSingleClickListener {
            listener.onOpenWebsite(model)
        }
        holder.textName.setOnSingleClickListener {
            listener.onEditMacro(model)
        }
        holder.textUrl.setOnSingleClickListener {
            listener.onOpenWebsite(model)
        }
        holder.chipError.setOnSingleClickListener {
            listener.onViewHistories(model)
        }
        holder.chipChange.setOnSingleClickListener {
            listener.onViewHistories(model)
        }
        holder.textDate.setOnSingleClickListener {
            listener.onViewHistories(model)
        }
        holder.imagePlay.setOnSingleClickListener {
            listener.onExecuteMacro(model)
        }
        holder.imageMore.setOnSingleClickListener {
            openMoreMenu(holder, model)
        }
    }

    private fun openMoreMenu(holder: ListViewHolder, model: Macro) {
        holder.imageMore.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(Menu.NONE, ACTION_EDIT_MACRO, Menu.NONE, applicationContext.resources.getString(R.string.action_edit_macro))
            menu.add(Menu.NONE, ACTION_VIEW_HISTORIES, Menu.NONE, applicationContext.resources.getString(R.string.action_view_histories))
            menu.add(Menu.NONE, ACTION_OPEN_WEBSITE, Menu.NONE, applicationContext.resources.getString(R.string.action_open_website))
            menu.findItem(ACTION_EDIT_MACRO).setOnMenuItemClickListener {
                listener.onEditMacro(model)
                true
            }
            menu.findItem(ACTION_VIEW_HISTORIES).setOnMenuItemClickListener {
                listener.onViewHistories(model)
                true
            }
            menu.findItem(ACTION_OPEN_WEBSITE).setOnMenuItemClickListener {
                listener.onOpenWebsite(model)
                true
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.imageMore.showContextMenu(holder.imageMore.pivotX, holder.imageMore.pivotY)
        } else {
            holder.imageMore.showContextMenu()
        }
        holder.imageMore.setOnCreateContextMenuListener(null)
    }
}
