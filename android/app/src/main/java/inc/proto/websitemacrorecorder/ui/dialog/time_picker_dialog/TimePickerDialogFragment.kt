package inc.proto.websitemacrorecorder.ui.dialog.time_picker_dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerDialogFragment(
    private val hourOfDay: Int,
    private val minute: Int,
    private val listener: Listener
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface Listener {
        fun onSelectedTime(hourOfDay: Int, minute: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context, this, hourOfDay, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelectedTime(hourOfDay, minute)
    }
}
