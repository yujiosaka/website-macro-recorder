package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.functions.HttpsCallableResult
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.Macro.Companion.ORDER_UPDATED_AT_DESC_VALUE
import inc.proto.websitemacrorecorder.repository.MacroRepository
import java.util.*

class ListViewModel @ViewModelInject constructor(
    private val sharedPreferences: SharedPreferences,
    private val macroRepository: MacroRepository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    val order = MutableLiveData<Int>().also {
        it.value = getOrder()
    }

    fun buildMacro(): Macro {
        return Macro(
            id = macroRepository.getId(),
            acceptLanguage = Locale.getDefault().language,
            deviceScaleFactor = applicationContext.resources.displayMetrics.density
        )
    }

    fun executeMacro(id: String): Task<HttpsCallableResult> {
        return macroRepository.execute(id)
    }

    fun getAllMacros(uid: String): Query {
        return macroRepository.getAll(uid).orderBy(getOrder())
    }

    private fun getOrder(): Int {
        return sharedPreferences.getInt("ORDER", ORDER_UPDATED_AT_DESC_VALUE)
    }

    private fun Query.orderBy(order: Int): Query {
        return when (order) {
            Macro.ORDER_UPDATED_AT_DESC_VALUE -> this.orderBy("updatedAt", Query.Direction.DESCENDING)
            Macro.ORDER_UPDATED_AT_ASC_VALUE -> this.orderBy("updatedAt", Query.Direction.ASCENDING)
            Macro.ORDER_CREATED_AT_DESC_VALUE -> this.orderBy("createdAt", Query.Direction.DESCENDING)
            Macro.ORDER_CREATED_AT_ASC_VALUE -> this.orderBy("createdAt", Query.Direction.ASCENDING)
            Macro.ORDER_EXECUTED_AT_DESC_VALUE -> this.orderBy("executedAt", Query.Direction.DESCENDING)
            Macro.ORDER_EXECUTED_AT_ASC_VALUE -> this.orderBy("executedAt", Query.Direction.ASCENDING)
            else -> throw IllegalArgumentException("Unknown order! $order")
        }
    }
}
