package inc.proto.websitemacrorecorder.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MacroRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    companion object {
        private const val CREATE_NAME = "create"
        private const val EXECUTE_NAME = "execute"
        private const val SCREENSHOT_NAME = "screenshot"
        private const val COLLECTION_NAME = "macros"
        private const val UID_NAME = "uid"
        private const val CREATE_TIMEOUT_SECONDS = 30L
        private const val EXECUTE_TIMEOUT_SECONDS = 300L
        private const val SCREENSHOT_TIMEOUT_SECONDS = 300L
    }

    fun create(macro: Map<String, Any?>): Task<HttpsCallableResult> {
        return functions
            .getHttpsCallable(CREATE_NAME)
            .withTimeout(CREATE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .call(macro)
    }

    fun execute(id: String): Task<HttpsCallableResult> {
        return functions
            .getHttpsCallable(EXECUTE_NAME)
            .withTimeout(EXECUTE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .call(id)
    }

    fun screenshot(macro: Map<String, Any?>): Task<HttpsCallableResult> {
        return functions
            .getHttpsCallable(SCREENSHOT_NAME)
            .withTimeout(SCREENSHOT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .call(macro)
    }

    fun getAll(uid: String): Query {
        return firestore
            .collection(COLLECTION_NAME)
            .whereEqualTo(UID_NAME, uid)
    }

    fun update(id: String, macro: Map<String, Any?>): Task<Void> {
        val documentReference = firestore
            .collection(COLLECTION_NAME)
            .document(id)
        return documentReference.update(macro)
    }

    fun delete(id: String): Task<Void> {
        val documentReference = firestore
            .collection(COLLECTION_NAME)
            .document(id)
        return documentReference.delete()
    }

    fun getId(): String {
        val doc = firestore
            .collection(COLLECTION_NAME)
            .document()
        return doc.id
    }
}
