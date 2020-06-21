package inc.proto.websitemacrorecorder.ui.dialog.time_picker_dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface Listener {
        fun onSelectedTime(hourOfDay: Int, minute: Int)
    }

    private lateinit var listener: Listener
    private var hourOfDay: Int? = null
    private var minute: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context, this, hourOfDay!!, minute!!, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelectedTime(hourOfDay, minute)
    }

    fun init(_listener: Listener, _checkHour: Int, _checkMinute: Int) {
        listener = _listener
        hourOfDay = _checkHour
        minute = _checkMinute
    }
}
