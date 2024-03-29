package inc.proto.websitemacrorecorder.data

import android.graphics.Rect
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.Timestamp
import inc.proto.websitemacrorecorder.BR
import java.io.Serializable
import kotlin.collections.ArrayList

class Macro(
    // Basic
    id: String = "",
    uid: String = "",
    name: String = "",
    url: String = DEFAULT_URL,
    screenshotUrl: String = "",

    // Schedule
    scheduleFrequency: Int = DEFAULT_SCHEDULE_FREQUENCY,
    scheduleHour: Int = DEFAULT_SCHEDULE_HOUR,
    scheduleMinute: Int = DEFAULT_SCHEDULE_MINUTE,
    scheduleSunday: Boolean = false,
    scheduleMonday: Boolean = true,
    scheduleTuesday: Boolean = true,
    scheduleWednesday: Boolean = true,
    scheduleThursday: Boolean = true,
    scheduleFriday: Boolean = true,
    scheduleSaturday: Boolean = false,
    enableSchedule: Boolean = true,

    // Notification
    notifySuccess: Boolean = true,
    notifyFailure: Boolean = true,

    // Update check
    checkEntirePage: Boolean = false,
    checkSelectedArea: Boolean = false,
    isEntirePageUpdated: Boolean = false,
    isSelectedAreaUpdated: Boolean = false,

    // Execution result
    isFailure: Boolean = false,

    // Device
    acceptLanguage: String = "",
    userAgent: String = "",
    viewportHeight: Int = 0,
    viewportWidth: Int = 0,
    deviceScaleFactor: Float = 0f,

    // Selected area
    selectedAreaLeft: Int? = null,
    selectedAreaTop: Int? = null,
    selectedAreaRight: Int? = null,
    selectedAreaBottom: Int? = null,

    // Children
    events: ArrayList<MacroEvent> = arrayListOf(),
    histories: ArrayList<MacroHistory> = arrayListOf(),

    // Date
    createdAt: Timestamp? = null,
    updatedAt: Timestamp? = null,
    executedAt: Timestamp? = null
) : Serializable, BaseObservable() {
    companion object {
        private const val SCHEDULE_FORMAT = "%1$01d:%2$02d"

        const val DEFAULT_URL = "https://"
        const val DEFAULT_SCHEDULE_FREQUENCY = 0
        const val DEFAULT_SCHEDULE_HOUR = 10
        const val DEFAULT_SCHEDULE_MINUTE = 0
        const val ORDER_UPDATED_AT_DESC_VALUE = 0
        const val ORDER_UPDATED_AT_ASC_VALUE = 1
        const val ORDER_CREATED_AT_DESC_VALUE = 2
        const val ORDER_CREATED_AT_ASC_VALUE = 3
        const val ORDER_EXECUTED_AT_DESC_VALUE = 4
        const val ORDER_EXECUTED_AT_ASC_VALUE = 5
    }

    @Bindable
    var id = id
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @Bindable
    var uid = uid
        set(value) {
            field = value
            notifyPropertyChanged(BR.uid)
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
            notifyPropertyChanged(BR.scheduleHour)
        }

    @Bindable
    var scheduleMinute = scheduleMinute
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleMinute)
        }

    @Bindable
    var scheduleSunday = scheduleSunday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleSunday)
        }

    @Bindable
    var scheduleMonday = scheduleMonday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleMonday)
        }

    @Bindable
    var scheduleTuesday = scheduleTuesday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleTuesday)
        }

    @Bindable
    var scheduleWednesday = scheduleWednesday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleWednesday)
        }

    @Bindable
    var scheduleThursday = scheduleThursday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleThursday)
        }

    @Bindable
    var scheduleFriday = scheduleFriday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleFriday)
        }

    @Bindable
    var scheduleSaturday = scheduleSaturday
        set(value) {
            field = value
            notifyPropertyChanged(BR.scheduleSaturday)
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
    var isEntirePageUpdated = isEntirePageUpdated
        set(value) {
            field = value
            notifyPropertyChanged(BR.isEntirePageUpdated)
        }

    @Bindable
    var isSelectedAreaUpdated = isSelectedAreaUpdated
        set(value) {
            field = value
            notifyPropertyChanged(BR.isSelectedAreaUpdated)
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

    @Bindable
    var events = events
        set(value) {
            field = value
            notifyPropertyChanged(BR.events)
        }

    @Bindable
    var histories = histories
        set(value) {
            field = value
            notifyPropertyChanged(BR.histories)
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

    fun isScheduled(): Boolean {
        return scheduleSunday ||
                scheduleMonday ||
                scheduleTuesday ||
                scheduleWednesday ||
                scheduleThursday ||
                scheduleFriday ||
                scheduleSaturday
    }

    fun getScheduledDays(): Int {
        var days = 0
        if (scheduleSunday) days += 1
        if (scheduleMonday) days += 1
        if (scheduleTuesday) days += 1
        if (scheduleWednesday) days += 1
        if (scheduleThursday) days += 1
        if (scheduleFriday) days += 1
        if (scheduleSaturday) days += 1
        return days
    }

    fun isScheduledEveryday(): Boolean {
        return scheduleSunday &&
                scheduleMonday &&
                scheduleTuesday &&
                scheduleWednesday &&
                scheduleThursday &&
                scheduleFriday &&
                scheduleSaturday
    }

    fun isScheduledWeekdays(): Boolean {
        return !scheduleSunday &&
                scheduleMonday &&
                scheduleTuesday &&
                scheduleWednesday &&
                scheduleThursday &&
                scheduleFriday &&
                !scheduleSaturday
    }

    fun isScheduledWeekends(): Boolean {
        return scheduleSunday &&
                !scheduleMonday &&
                !scheduleTuesday &&
                !scheduleWednesday &&
                !scheduleThursday &&
                !scheduleFriday &&
                scheduleSaturday
    }

    fun getScheduleTime(): String {
        return SCHEDULE_FORMAT.format(scheduleHour, scheduleMinute)
    }

    fun isAreaSelected(): Boolean {
        return selectedAreaLeft != null &&
                selectedAreaTop != null &&
                selectedAreaRight != null &&
                selectedAreaBottom != null
    }

    fun getSelectedAreaSize(): String {
        if (!isAreaSelected()) return ""

        val selectedAreaWidth = selectedAreaRight!! - selectedAreaLeft!!
        val selectedAreaHeight = selectedAreaBottom!! - selectedAreaTop!!
        return "$selectedAreaWidth x $selectedAreaHeight"
    }

    fun getSelectedAreaRect(): Rect? {
        if (!isAreaSelected()) return null

        return Rect(selectedAreaLeft!!, selectedAreaTop!!, selectedAreaRight!!, selectedAreaBottom!!)
    }

    fun setSelectedAreaRect(rect: Rect?) {
        selectedAreaLeft = rect?.left
        selectedAreaTop = rect?.top
        selectedAreaRight = rect?.right
        selectedAreaBottom = rect?.bottom
    }
}
