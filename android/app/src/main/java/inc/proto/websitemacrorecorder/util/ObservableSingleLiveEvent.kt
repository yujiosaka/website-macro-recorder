package inc.proto.websitemacrorecorder.util

import androidx.annotation.MainThread
import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


// This class notifies changes to observers when any value of the property changes
class ObservableSingleLiveEvent<T : BaseObservable>: MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    private val callback = object: Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            value = value
        }
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (!pending.compareAndSet(true, false)) return@Observer

            observer.onChanged(t)
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)

        super.setValue(value)

        value?.removeOnPropertyChangedCallback(callback)
        value?.notifyChange()
        value?.addOnPropertyChangedCallback(callback)
    }

    @MainThread
    fun call() {
        value = null
    }
}
