package inc.proto.websitemacrorecorder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import inc.proto.websitemacrorecorder.BR
import java.io.Serializable

class Macro(
    id: String = "",
    name: String = "",
    url: String = "https://",
    screenshotUrl: String = "",
    scheduleFrequency: Int = 0,
    scheduleHour: Int = 10,
    scheduleMinute: Int = 0,
    enableSchedule: Boolean = true,
    notifySuccess: Boolean = true,
    notifyFailure: Boolean = true,
    isFailure: Boolean = false,
    acceptLanguage: String = "en",
    userAgent: String = "",
    height: Int = 0,
    width: Int = 0,
    deviceScaleFactor: Float = 1f,
    events: ArrayList<MacroEvent> = arrayListOf(),
    createdAt: Timestamp? = null,
    updatedAt: Timestamp? = null
) : Serializable, BaseObservable() {
    companion object {
        const val ORDER_UPDATED_AT_DESC_VALUE = 0
        const val ORDER_UPDATED_AT_ASC_VALUE = 1
        const val ORDER_CREATED_AT_DESC_VALUE = 2
        const val ORDER_CREATED_AT_ASC_VALUE = 3
    }

    @Bindable
    var id = id
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @Bindable
    var name = name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var url = url
        set(value) {
            field = value
            notifyPropertyChanged(BR.url)
        }

    @Bindable
    var screenshotUrl = screenshotUrl
        set(value) {
            field = value
            notifyPropertyChanged(BR.screenshotUrl)
        }

    @Bindable
    var scheduleFrequency = scheduleFrequency
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleFrequency)
        }

    @Bindable
    var scheduleHour = scheduleHour
        set(value) {
            field = value
            notifyPropertyChanged(BR.schedule)
            notifyPropertyChanged(BR.scheduleHour)
        }

    @Bindable
    var scheduleMinute = scheduleMinute
        set(value) {
            field = value
            notifyPropertyChanged(BR.schedule)
            notifyPropertyChanged(BR.scheduleMinute)
        }

    @get:Bindable
    var schedule: String
        get() = "%1$01d:%2$02d".format(scheduleHour, scheduleMinute)
        set(value) {
            val pair = value.split(":").map { it.toInt() }
            scheduleHour = pair[0]
            scheduleMinute = pair[1]
            notifyPropertyChanged(BR.scheduleHour)
            notifyPropertyChanged(BR.scheduleMinute)
        }

    @Bindable
    var enableSchedule = enableSchedule
        set(value) {
            field = value
            notifyPropertyChanged(BR.enableSchedule)
        }

    @Bindable
    var notifySuccess = notifySuccess
        set(value) {
            field = value
            notifyPropertyChanged(BR.notifySuccess)
        }

    @Bindable
    var notifyFailure = notifyFailure
        set(value) {
            field = value
            notifyPropertyChanged(BR.notifyFailure)
        }

    @Bindable
    var isFailure = isFailure
        set(value) {
            field = value
            notifyPropertyChanged(BR.isFailure)
        }

    @Bindable
    var acceptLanguage = acceptLanguage
        set(value) {
            field = value
            notifyPropertyChanged(BR.acceptLanguage)
        }

    @Bindable
    var userAgent = userAgent
        set(value) {
            field = value
            notifyPropertyChanged(BR.userAgent)
        }

    @Bindable
    var height = height
        set(value) {
            field = value
            notifyPropertyChanged(BR.height)
        }

    @Bindable
    var width = width
        set(value) {
            field = value
            notifyPropertyChanged(BR.width)
        }

    @Bindable
    var deviceScaleFactor = deviceScaleFactor
        set(value) {
            field = value
            notifyPropertyChanged(BR.deviceScaleFactor)
        }

    @Bindable
    var events = events
        set(value) {
            field = value
            notifyPropertyChanged(BR.events)
        }

    @Bindable
    var createdAt = createdAt
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdAt)
        }

    @Bindable
    var updatedAt = updatedAt
        set(value) {
            field = value
            notifyPropertyChanged(BR.updatedAt)
        }
}
