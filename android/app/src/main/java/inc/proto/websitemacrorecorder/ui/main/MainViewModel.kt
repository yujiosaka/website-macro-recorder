package inc.proto.websitemacrorecorder.ui.main

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.Macro.Companion.DEFAULT_URL
import inc.proto.websitemacrorecorder.repository.MacroRepository
import java.util.*

class MainViewModel @ViewModelInject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val macroRepository: MacroRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    fun buildMacro(url: String?): Macro {
        return Macro(
            id = macroRepository.getId(),
            url = url ?: DEFAULT_URL,
            acceptLanguage = Locale.getDefault().language,
            deviceScaleFactor = applicationContext.resources.displayMetrics.density
        )
    }

    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }

    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.removeAuthStateListener(listener)
    }
}
