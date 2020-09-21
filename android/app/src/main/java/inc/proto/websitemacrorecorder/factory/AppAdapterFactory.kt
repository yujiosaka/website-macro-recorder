package inc.proto.websitemacrorecorder.factory

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsAdapter
import inc.proto.websitemacrorecorder.ui.list.ListAdapter
import inc.proto.websitemacrorecorder.ui.show_histories.ShowHistoriesAdapter
import inc.proto.websitemacrorecorder.util.TextFormatter
import java.lang.IllegalArgumentException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AppAdapterFactory @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
    private val macroRepository: MacroRepository,
    private val textFormatter: TextFormatter
) {
    fun instantiate(
        className: String,
        args: AppAdapterArgs
    ) : RecyclerView.Adapter<*> {
        return when (className) {
            ListAdapter::class.java.name -> {
                val listAdapterArgs = args as ListAdapterArgs
                ListAdapter(
                    applicationContext,
                    sharedPreferences,
                    macroRepository,
                    textFormatter,
                    listAdapterArgs.options,
                    listAdapterArgs.listener
                )
            }
            EditEventsAdapter::class.java.name -> {
                val editEventsAdapterArgs = args as EditEventsAdapterArgs
                EditEventsAdapter(
                    applicationContext,
                    editEventsAdapterArgs.events,
                    editEventsAdapterArgs.listener
                )
            }
            ShowHistoriesAdapter::class.java.name -> {
                val viewHistoriesAdapterArgs = args as ViewHistoriesAdapterArgs
                ShowHistoriesAdapter(
                    applicationContext,
                    viewHistoriesAdapterArgs.histories,
                    viewHistoriesAdapterArgs.listener
                )
            }
            else -> {
                throw IllegalArgumentException("Unknown class name $className")
            }
        }
    }
}
