package inc.proto.websitemacrorecorder.ui.edit_events_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditEventsDialogBinding


class EditEventsDialog : DialogFragment() {
    interface Listener {
        fun onAddTimer(value: String)
    }

    private lateinit var _binding: FragmentEditEventsDialogBinding
    private lateinit var _vm: EditEventsDialogViewModel
    private lateinit var _listener: Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentEditEventsDialogBinding.inflate(LayoutInflater.from(activity), null, false)
        _vm = ViewModelProviders.of(this).get(EditEventsDialogViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.text_add_timer))
            .setView(_binding.root)
            .setPositiveButton(getString(R.string.text_add)) { _, _ ->
                _listener.onAddTimer(_vm.seconds)
            }
            .setNegativeButton(getString(R.string.text_close), null)
            .create()
    }

    fun setListener(listener: Listener) {
        _listener = listener
    }
}
