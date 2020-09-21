package inc.proto.websitemacrorecorder.util

import android.content.Context
import android.text.format.DateFormat
import com.google.firebase.functions.FirebaseFunctionsException
import dagger.hilt.android.qualifiers.ApplicationContext
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import java.util.*
import javax.inject.Inject

class TextFormatter @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    fun firebaseFunctionExceptionToMessage(exception: FirebaseFunctionsException): String {
        return when (exception.code) {
            FirebaseFunctionsException.Code.INVALID_ARGUMENT -> {
                applicationContext.resources.getString(R.string.error_invalid_argument)
            }
            FirebaseFunctionsException.Code.UNAUTHENTICATED -> {
                applicationContext.resources.getString(R.string.error_unauthenticated)
            }
            FirebaseFunctionsException.Code.UNAVAILABLE -> {
                applicationContext.resources.getString(R.string.error_unavailable)
            }
            FirebaseFunctionsException.Code.INTERNAL -> {
                applicationContext.resources.getString(R.string.error_internal)
            }
            FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> {
                applicationContext.resources.getString(R.string.error_deadline_exceeded)
            }
            else -> {
                applicationContext.resources.getString(R.string.error_unknown)
            }
        }
    }

    fun macroToSchedule(macro: Macro): String {
        val frequencies = applicationContext.resources.getStringArray(R.array.text_frequency_array)
        return if (macro.scheduleFrequency == 1) {
            val schedule = when {
                macro.isScheduledEveryday() -> {
                    applicationContext.resources.getString(R.string.text_schedule_everyday)
                }
                macro.isScheduledWeekdays() -> {
                    applicationContext.resources.getString(R.string.text_schedule_weekdays)
                }
                macro.isScheduledWeekends() -> {
                    applicationContext.resources.getString(R.string.text_schedule_weekends)
                }
                macro.getScheduledDays() == 1 && macro.scheduleSunday -> {
                    applicationContext.resources.getString(R.string.text_schedule_sunday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleMonday -> {
                    applicationContext.resources.getString(R.string.text_schedule_monday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleTuesday -> {
                    applicationContext.resources.getString(R.string.text_schedule_tuesday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleWednesday -> {
                    applicationContext.resources.getString(R.string.text_schedule_wednesday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleThursday -> {
                    applicationContext.resources.getString(R.string.text_schedule_thursday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleFriday -> {
                    applicationContext.resources.getString(R.string.text_schedule_friday)
                }
                macro.getScheduledDays() == 1 && macro.scheduleSaturday -> {
                    applicationContext.resources.getString(R.string.text_schedule_saturday)
                }
                else -> {
                    applicationContext.resources.getString(R.string.text_schedule_days, macro.getScheduledDays().toString())
                }
            }
            applicationContext.resources.getString(R.string.text_schedule, schedule, macro.getScheduleTime())
        } else {
            frequencies[macro.scheduleFrequency]
        }
    }

    fun dateToDateTime(date: Date): String {
        val dateString = DateFormat.getMediumDateFormat(applicationContext).format(date)
        val timeString = DateFormat.getTimeFormat(applicationContext).format(date)

        return "$dateString $timeString"
    }
}
