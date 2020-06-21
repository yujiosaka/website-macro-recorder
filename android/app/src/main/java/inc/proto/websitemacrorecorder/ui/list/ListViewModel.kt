package inc.proto.websitemacrorecorder.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import inc.proto.websitemacrorecorder.data.Macro

class ListViewModel : ViewModel() {
    val order = MutableLiveData<Int>().also {
        it.value = Macro.ORDER_CREATED_AT_DESC_VALUE
    }
}
