package inc.proto.websitemacrorecorder.factory

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsCallback
import java.lang.IllegalArgumentException
import javax.inject.Inject

class AppCallbackFactory @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    fun instantiate(
        className: String,
        args: AppCallbackArgs
    ): ItemTouchHelper.SimpleCallback {
        return when (className) {
            EditEventsCallback::class.java.name -> {
                val editEventsCallbackArgs = args as EditEventsCallbackArgs
                EditEventsCallback(
                    editEventsCallbackArgs.dragDirs,
                    editEventsCallbackArgs.swipeDirs,
                    editEventsCallbackArgs.vm,
                    editEventsCallbackArgs.adapter,
                    applicationContext
                )
            }
            else -> {
                throw IllegalArgumentException("Unknown class name $className")
            }
        }
    }
}
