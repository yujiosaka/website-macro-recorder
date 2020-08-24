package inc.proto.websitemacrorecorder.data

import android.graphics.Rect
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import inc.proto.websitemacrorecorder.App
import inc.proto.websitemacrorecorder.BR
import inc.proto.websitemacrorecorder.repository.MacroRepository
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Macro(
    id: String = MacroRepository().getId(),
    name: String = "",
    url: String = "https://",
    screenshotUrl: String = "",
    scheduleFrequency: Int = 0,
    scheduleHour: Int = 10,
    scheduleMinute: Int = 0,
    enableSchedule: Boolean = true,
    notifySuccess: Boolean = true,
    notifyFailure: Boolean = true,
    checkEntirePage: Boolean = false,
    checkSelectedArea: Boolean = false,
    isFailure: Boolean = false,
    acceptLanguage: String = Locale.getDefault().language,
    userAgent: String = "",
    viewportHeight: Int = 0,
    viewportWidth: Int = 0,
    deviceScaleFactor: Float = App.context.resources.displayMetrics.density,
    selectedAreaLeft: Int? = null,
    selectedAreaTop: Int? = null,
    selectedAreaRight: Int? = null,
    selectedAreaBottom: Int? = null,
    events: ArrayList<MacroEvent> = arrayListOf(),
    createdAt: Timestamp? = null,
    updatedAt: Timestamp? = null,
    executedAt: Timestamp? = null
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
    var checkEntirePage = checkEntirePage
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkEntirePage)
        }

    @Bindable
    var checkSelectedArea = checkSelectedArea
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkSelectedArea)
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
    var viewportHeight = viewportHeight
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewportHeight)
        }

    @Bindable
    var viewportWidth = viewportWidth
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewportWidth)
        }

    @Bindable
    var deviceScaleFactor = deviceScaleFactor
        set(value) {
            field = value
            notifyPropertyChanged(BR.deviceScaleFactor)
        }

    @Bindable
    var selectedAreaLeft = selectedAreaLeft
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedAreaLeft)
        }

    @Bindable
    var selectedAreaTop = selectedAreaTop
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedAreaTop)
        }

    @Bindable
    var selectedAreaRight = selectedAreaRight
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedAreaRight)
        }

    @Bindable
    var selectedAreaBottom = selectedAreaBottom
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedAreaBottom)
        }

    var isAreaSelected = false
        get() {
            return selectedAreaLeft != null &&
                    selectedAreaTop != null &&
                    selectedAreaRight != null &&
                    selectedAreaBottom != null
        }

    var selectedAreaSize = ""
        get() {
            if (!isAreaSelected) return ""
            val selectedAreaWidth = selectedAreaRight!! - selectedAreaLeft!!
            val selectedAreaHeight = selectedAreaBottom!! - selectedAreaTop!!
            return "$isAreaSelected x $selectedAreaHeight"
        }

    var selectedAreaRect: Rect?
        get() {
            if (!isAreaSelected) return null
            return Rect(selectedAreaLeft!!, selectedAreaTop!!, selectedAreaRight!!, selectedAreaBottom!!)
        }
        set(value) {
            selectedAreaLeft = value?.left
            selectedAreaTop = value?.top
            selectedAreaRight = value?.right
            selectedAreaBottom = value?.bottom
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

    @Bindable
    var executedAt = executedAt
        set(value) {
            field = value
            notifyPropertyChanged(BR.executedAt)
        }
}
