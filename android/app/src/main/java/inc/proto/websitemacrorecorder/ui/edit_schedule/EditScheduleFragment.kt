package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.databinding.FragmentEditScheduleBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.ui.dialog.time_picker_dialog.TimePickerDialogFragment

class EditScheduleFragment : Fragment(), TimePickerDialogFragment.Listener {
    private val vm: EditScheduleViewModel by lazy {
        ViewModelProvider(this, EditScheduleViewModelFactory(args.macro)).get(EditScheduleViewModel::class.java)
    }
    private val args: EditScheduleFragmentArgs by navArgs()
    private val macroRepository = MacroRepository()
    private lateinit var binding: FragmentEditScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editSchedule.setOnClickListener {
            if (activity == null) return@setOnClickListener
            val dialog = TimePickerDialogFragment()
            dialog.init(this, vm.macro.value!!.scheduleHour, vm.macro.value!!.scheduleMinute)
            dialog.show(requireActivity().supportFragmentManager, "time_picker")
        }
        binding.editScheduleFrequency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    vm.macro.value!!.scheduleFrequency = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.buttonSave.setOnClickListener {
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleFrequency" to vm.macro.value!!.scheduleFrequency,
                "scheduleHour" to vm.macro.value!!.scheduleHour,
                "scheduleMinute" to vm.macro.value!!.scheduleMinute,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            findNavController().popBackStack()
        }
    }

    override fun onSelectedTime(hourOfDay: Int, minute: Int) {
        vm.macro.value!!.scheduleHour = hourOfDay
        vm.macro.value!!.scheduleMinute = minute
    }

}
