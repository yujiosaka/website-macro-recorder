package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.databinding.FragmentEditScheduleBinding
import inc.proto.websitemacrorecorder.ui.time_picker_dialog.TimePickerDialogFragment

class EditScheduleFragment : Fragment(), TimePickerDialogFragment.Listener {

    private val _args: EditScheduleFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditScheduleBinding
    private lateinit var _vm: EditScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditScheduleViewModelFactory(_args.macro)

        _binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditScheduleViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.editSchedule.setOnClickListener {
            val dialog = TimePickerDialogFragment()
            dialog.init(this, _vm.scheduleHour, _vm.scheduleMinute)
            dialog.show(requireActivity().supportFragmentManager, "time_picker")
        }
        _binding.editScheduleFrequency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    _vm.scheduleFrequency = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        _binding.buttonSave.setOnClickListener {
            val action = EditScheduleFragmentDirections.actionEditScheduleFragmentToEditFragment(_vm.macro)
            findNavController().navigate(action)
        }
    }

    override fun onSelectedTime(hourOfDay: Int, minute: Int) {
        _vm.scheduleHour = hourOfDay
        _vm.scheduleMinute = minute
    }

}
