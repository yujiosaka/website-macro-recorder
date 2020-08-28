package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
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
import android.text.format.DateFormat
import androidx.preference.PreferenceManager
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class ListAdapter(fragment: ListFragment, options: FirestoreRecyclerOptions<Macro>) : FirestoreRecyclerAdapter<Macro, ListViewHolder>(options) {
    companion object {
        private const val ACTION_EDIT_MACRO = 1
    }

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    private val macroRepository = MacroRepository()
    private val fragment = fragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListViewHolder {
        context = parent.context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val view = LayoutInflater.from(context).inflate(R.layout.item_macro, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Macro) {
        Glide.with(context).load(model.screenshotUrl).into(holder.imageScreenshot)
        val frequencies = context.resources.getStringArray(R.array.text_frequency_array)
        val schedule = frequencies[model.scheduleFrequency]
        holder.textSchedule.text = if (model.scheduleFrequency == 1) {
            context.resources.getString(R.string.text_schedule, schedule, model.schedule)
        } else {
            schedule
        }
        holder.editEnableSchedule.visibility = if (model.scheduleFrequency == 0) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        holder.editEnableSchedule.setOnCheckedChangeListener(null)
        holder.editEnableSchedule.isChecked = model.enableSchedule
        holder.editEnableSchedule.setOnCheckedChangeListener { _, isChecked ->
            macroRepository.update(model.id, mapOf(
                "enableSchedule" to isChecked,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }
        holder.textName.text = if (model.name != "") {
            model.name
        } else {
            context.resources.getString(R.string.text_no_name)

        }
        holder.textUrl.text = model.url
        val order = sharedPreferences.getInt("ORDER", 0)
        holder.textDate.text = when (order) {
            Macro.ORDER_UPDATED_AT_DESC_VALUE, Macro.ORDER_UPDATED_AT_ASC_VALUE -> {
                if (model.updatedAt != null) {
                    DateFormat.getMediumDateFormat(context).format(model.updatedAt!!.toDate())
                } else {
                    context.resources.getString(R.string.text_date_latest)
                }
            }
            Macro.ORDER_CREATED_AT_DESC_VALUE, Macro.ORDER_CREATED_AT_ASC_VALUE -> {
                if (model.createdAt != null) {
                    DateFormat.getMediumDateFormat(context).format(model.createdAt!!.toDate())
                } else {
                    context.resources.getString(R.string.text_date_latest)
                }
            }
            else -> {
                if (model.executedAt != null) {
                    DateFormat.getMediumDateFormat(context).format(model.executedAt!!.toDate())
                } else {
                    context.resources.getString(R.string.text_date_latest)
                }
            }
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
        holder.imagePlay.setOnSingleClickListener {
            fragment.executeMacro(model)
        }
        holder.imageMore.setOnSingleClickListener {
            holder.imageMore.setOnCreateContextMenuListener { menu, _, _ ->
                menu.add(Menu.NONE, ACTION_EDIT_MACRO, Menu.NONE, context.resources.getString(R.string.action_edit_macro))
                menu.findItem(ACTION_EDIT_MACRO).setOnMenuItemClickListener {
                    fragment.editMacro(model)
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
}
