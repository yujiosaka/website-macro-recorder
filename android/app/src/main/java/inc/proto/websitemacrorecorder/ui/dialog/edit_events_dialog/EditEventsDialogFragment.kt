package inc.proto.websitemacrorecorder.ui.dialog.edit_events_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.databinding.FragmentEditEventsDialogBinding

class EditEventsDialog : DialogFragment() {
    interface Listener {
        fun onAddTimer(value: String)
    }

    private val vm: EditEventsDialogViewModel by lazy {
        ViewModelProvider(this).get(EditEventsDialogViewModel::class.java)
    }
    private lateinit var binding: FragmentEditEventsDialogBinding
    private lateinit var listener: Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentEditEventsDialogBinding.inflate(LayoutInflater.from(activity), null, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        bindViewModel()
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_add_timer))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.text_add)) { _, _ ->
                listener.onAddTimer(vm.seconds.value!!)
            }
            .setNegativeButton(getString(R.string.text_close), null)
            .create()
    }

    fun setListener(_listener: Listener) {
        listener = _listener
    }

    private fun bindViewModel() {
        vm.seconds.observe(this, Observer {
            try {
                val intValue = Integer.parseInt(it)
                if (intValue > MacroEvent.MAX_WAIT_VALUE) {
                    vm.seconds.value = MacroEvent.MAX_WAIT_VALUE.toString()
                }
                if (intValue < MacroEvent.MIN_WAIT_VALUE) {
                    vm.seconds.value = MacroEvent.MIN_WAIT_VALUE.toString()
                }
            } catch (e: NumberFormatException) {
                vm.seconds.value = MacroEvent.DEFAULT_WAIT_VALUE.toString()
            }
        })
    }
}
