package inc.proto.websitemacrorecorder.factory

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.ui.confirm.ConfirmFragment
import inc.proto.websitemacrorecorder.ui.edit.EditFragment
import inc.proto.websitemacrorecorder.ui.edit_events.EditEventsFragment
import inc.proto.websitemacrorecorder.ui.edit_record.EditRecordFragment
import inc.proto.websitemacrorecorder.ui.list.ListFragment
import inc.proto.websitemacrorecorder.ui.tutorial5.Tutorial5Fragment
import inc.proto.websitemacrorecorder.ui.show_histories.ShowHistoriesFragment
import inc.proto.websitemacrorecorder.ui.show_history.ShowHistoryFragment
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.TextFormatter
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val appAdapterFactory: AppAdapterFactory,
    private val appCallbackFactory: AppCallbackFactory,
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val textFormatter: TextFormatter,
    private val helper: Helper
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            Tutorial5Fragment::class.java.name -> {
                Tutorial5Fragment(sharedPreferences)
            }
            ListFragment::class.java.name -> {
                ListFragment(
                    applicationContext,
                    appAdapterFactory,
                    sharedPreferences,
                    firebaseAuth,
                    textFormatter,
                    helper
                )
            }
            EditEventsFragment::class.java.name -> {
                EditEventsFragment(appAdapterFactory, appCallbackFactory, textFormatter)
            }
            EditRecordFragment::class.java.name -> {
                EditRecordFragment(applicationContext, helper)
            }
            EditFragment::class.java.name -> {
                EditFragment(helper)
            }
            ConfirmFragment::class.java.name -> {
                ConfirmFragment(textFormatter, helper)
            }
            ShowHistoriesFragment::class.java.name -> {
                ShowHistoriesFragment(appAdapterFactory)
            }
            ShowHistoryFragment::class.java.name -> {
                ShowHistoryFragment(textFormatter)
            }
            else -> {
                super.instantiate(classLoader, className)
            }
        }
    }
}
