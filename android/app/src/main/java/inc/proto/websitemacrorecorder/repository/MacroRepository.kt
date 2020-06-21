package inc.proto.websitemacrorecorder.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult

class MacroRepository {
    companion object {
        private const val SCREENSHOT_NAME = "screenshot"
        private const val SAVE_NAME = "save"
        private const val COLLECTION_NAME = "macros"
        private const val UID_NAME = "uid"
    }

    private val db = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()

    fun screenshot(macro: Map<String, Any?>): Task<HttpsCallableResult> {
        return functions.getHttpsCallable(SCREENSHOT_NAME).call(macro)
    }

    fun save(macro: Map<String, Any?>): Task<HttpsCallableResult> {
        return functions.getHttpsCallable(SAVE_NAME).call(macro)
    }

    fun update(id: String, macro: Map<String, Any?>): Task<Void> {
        val documentReference = db.collection(COLLECTION_NAME).document(id)
        return documentReference.update(macro)
    }

    fun get(id: String): DocumentReference {
        return db.collection(COLLECTION_NAME).document(id)
    }

    fun getAll(uid: String): Query {
        return db.collection(COLLECTION_NAME).whereEqualTo(UID_NAME, uid)
    }

    fun delete(id: String): Task<Void> {
        val documentReference = db.collection(COLLECTION_NAME).document(id)
        return documentReference.delete()
    }

    fun getId(): String {
        val doc = db.collection(COLLECTION_NAME).document()
        return doc.id
    }
}
