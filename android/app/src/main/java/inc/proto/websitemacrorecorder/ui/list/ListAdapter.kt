package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        private const val ACTION_RUN_MACRO = 1
        private const val ACTION_EDIT_MACRO = 2
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
        holder.textName.text = model.name
        holder.textUrl.text = model.url
        val order = sharedPreferences.getInt("ORDER", 0)
        holder.textDate.text = if (listOf(Macro.ORDER_UPDATED_AT_DESC_VALUE, Macro.ORDER_UPDATED_AT_ASC_VALUE).contains(order)) {
            if (model.updatedAt != null) {
                DateFormat.getMediumDateFormat(context).format(model.updatedAt!!.toDate())
            } else {
                context.resources.getString(R.string.text_date_latest)
            }
        } else {
            if (model.createdAt != null) {
                DateFormat.getMediumDateFormat(context).format(model.createdAt!!.toDate())
            } else {
                context.resources.getString(R.string.text_date_latest)
            }
        }
        holder.textError.visibility = if (model.isFailure) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        holder.card.setOnSingleClickListener {
            holder.card.setOnCreateContextMenuListener { menu, _, _ ->
                menu.setHeaderTitle(model.name)
                menu.add(Menu.NONE, ACTION_RUN_MACRO, Menu.NONE, context.resources.getString(R.string.action_run_macro))
                menu.add(Menu.NONE, ACTION_EDIT_MACRO, Menu.NONE, context.resources.getString(R.string.action_edit_macro))
                menu.findItem(ACTION_RUN_MACRO).setOnMenuItemClickListener {
                    true
                }
                menu.findItem(ACTION_EDIT_MACRO).setOnMenuItemClickListener {
                    val action = ListFragmentDirections.actionListFragmentToEditFragment(model)
                    fragment.findNavController().navigate(action)
                    true
                }
            }
            holder.card.showContextMenu()
            holder.card.setOnCreateContextMenuListener(null)
        }
    }
}
