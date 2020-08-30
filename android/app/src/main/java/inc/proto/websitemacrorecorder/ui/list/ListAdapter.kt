package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener


class ListAdapter(fragment: ListFragment, options: FirestoreRecyclerOptions<Macro>) : FirestoreRecyclerAdapter<Macro, ListViewHolder>(options) {
    companion object {
        private const val ACTION_EDIT_MACRO = 1
        private const val ACTION_VIEW_HISTORIES = 2
        private const val ACTION_OPEN_WEBSITE = 3
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
        holder.imageScreenshot.setOnSingleClickListener {
            fragment.openWebsite(model)
        }
        val frequencies = context.resources.getStringArray(R.array.text_frequency_array)
        holder.textSchedule.text = if (model.scheduleFrequency == 1) {
            val schedule = when {
                model.scheduleEveryday -> context.resources.getString(R.string.text_schedule_everyday)
                model.scheduleWeekdays -> context.resources.getString(R.string.text_schedule_weekdays)
                model.scheduleWeekends -> context.resources.getString(R.string.text_schedule_weekends)
                model.scheduleDays == 1 && model.scheduleSunday -> context.resources.getString(R.string.text_schedule_sunday)
                model.scheduleDays == 1 && model.scheduleMonday -> context.resources.getString(R.string.text_schedule_monday)
                model.scheduleDays == 1 && model.scheduleTuesday -> context.resources.getString(R.string.text_schedule_tuesday)
                model.scheduleDays == 1 && model.scheduleWednesday -> context.resources.getString(R.string.text_schedule_wednesday)
                model.scheduleDays == 1 && model.scheduleThursday -> context.resources.getString(R.string.text_schedule_thursday)
                model.scheduleDays == 1 && model.scheduleFriday -> context.resources.getString(R.string.text_schedule_friday)
                model.scheduleDays == 1 && model.scheduleSaturday -> context.resources.getString(R.string.text_schedule_saturday)
                else -> context.resources.getString(R.string.text_schedule_days, model.scheduleDays.toString())
            }
            context.resources.getString(R.string.text_schedule, schedule, model.schedule)
        } else {
            frequencies[model.scheduleFrequency]
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
        holder.textName.setOnSingleClickListener {
            fragment.editMacro(model)
        }
        holder.textUrl.text = model.url
        holder.textUrl.setOnSingleClickListener {
            fragment.openWebsite(model)
        }
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
        holder.chipError.setOnSingleClickListener {
            fragment.viewHistories(model)
        }
        holder.chipChange.visibility = if (model.isEntirePageUpdated || model.isSelectedAreaUpdated) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.chipChange.setOnSingleClickListener {
            fragment.viewHistories(model)
        }
        holder.textDate.setOnSingleClickListener {
            fragment.viewHistories(model)
        }
        holder.imagePlay.setOnSingleClickListener {
            fragment.executeMacro(model)
        }
        holder.imageMore.setOnSingleClickListener {
            holder.imageMore.setOnCreateContextMenuListener { menu, _, _ ->
                menu.add(Menu.NONE, ACTION_EDIT_MACRO, Menu.NONE, context.resources.getString(R.string.action_edit_macro))
                menu.add(Menu.NONE, ACTION_VIEW_HISTORIES, Menu.NONE, context.resources.getString(R.string.action_view_histories))
                menu.add(Menu.NONE, ACTION_OPEN_WEBSITE, Menu.NONE, context.resources.getString(R.string.action_open_website))
                menu.findItem(ACTION_EDIT_MACRO).setOnMenuItemClickListener {
                    fragment.editMacro(model)
                    true
                }
                menu.findItem(ACTION_VIEW_HISTORIES).setOnMenuItemClickListener {
                    fragment.viewHistories(model)
                    true
                }
                menu.findItem(ACTION_OPEN_WEBSITE).setOnMenuItemClickListener {
                    fragment.openWebsite(model)
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
