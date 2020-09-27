package inc.proto.websitemacrorecorder.ui.dialog.edit_events_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.databinding.FragmentEditEventsDialogBinding

@AndroidEntryPoint
class EditEventsDialog(private val listener: Listener) : DialogFragment() {
    interface Listener {
        fun onAddTimer(value: String)
    }

    private val vm by viewModels<EditEventsDialogViewModel>()
    private lateinit var binding: FragmentEditEventsDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)

        binding = FragmentEditEventsDialogBinding.inflate(inflater, null, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return createAlertDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
    }

    private fun setObservers() {
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

    private fun createAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_add_timer))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.text_add)) { _, _ ->
                listener.onAddTimer(vm.seconds.value!!)
            }
            .setNegativeButton(getString(R.string.text_close), null)
            .create()
    }
}
