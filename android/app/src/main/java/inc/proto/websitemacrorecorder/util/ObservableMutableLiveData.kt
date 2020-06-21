package inc.proto.websitemacrorecorder.util

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData

// This class notifies changes to observers when any value of the property changes
class ObservableMutableLiveData<T: BaseObservable>: MutableLiveData<T>() {
    private val callback = object: Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            value = value
        }
    }
    override fun setValue(value: T?) {
        super.setValue(value)
        value?.removeOnPropertyChangedCallback(callback)
        value?.notifyChange()
        value?.addOnPropertyChangedCallback(callback)
    }
}
