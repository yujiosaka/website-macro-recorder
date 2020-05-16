package inc.proto.websitemacrorecorder.ui.time_picker_dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface Listener {
        fun onSelectedTime(hourOfDay: Int, minute: Int)
    }

    private lateinit var _listener: Listener
    private var _hourOfDay: Int? = null
    private var _minute: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context, this, _hourOfDay!!, _minute!!, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        _listener.onSelectedTime(hourOfDay, minute)
    }

    fun init(listener: Listener, checkHour: Int, checkMinute: Int) {
        _listener = listener
        _hourOfDay = checkHour
        _minute = checkMinute
    }
}