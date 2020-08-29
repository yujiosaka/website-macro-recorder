package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditScheduleBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.ui.dialog.time_picker_dialog.TimePickerDialogFragment
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

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
        bindViewModel()
    }

    override fun onSelectedTime(hourOfDay: Int, minute: Int) {
        vm.macro.value!!.scheduleHour = hourOfDay
        vm.macro.value!!.scheduleMinute = minute
        macroRepository.update(vm.macro.value!!.id, mapOf(
            "scheduleHour" to vm.macro.value!!.scheduleHour,
            "scheduleMinute" to vm.macro.value!!.scheduleMinute,
            "updatedAt" to FieldValue.serverTimestamp()
        ))
        showUpdateNotification()
    }

    private fun bindViewModel() {
        binding.editSchedule.setOnSingleClickListener {
            if (activity == null) return@setOnSingleClickListener
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
                    if (vm.macro.value!!.scheduleFrequency == position) return
                    vm.macro.value!!.scheduleFrequency = position
                    macroRepository.update(vm.macro.value!!.id, mapOf(
                        "scheduleFrequency" to vm.macro.value!!.scheduleFrequency,
                        "updatedAt" to FieldValue.serverTimestamp()
                    ))
                    showUpdateNotification()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.chipScheduleSunday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleSunday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleSunday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleSunday = true
                binding.chipScheduleSunday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleSunday" to vm.macro.value!!.scheduleSunday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleMonday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleMonday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleMonday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleMonday = true
                binding.chipScheduleMonday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleMonday" to vm.macro.value!!.scheduleMonday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleTuesday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleTuesday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleTuesday = true
                binding.chipScheduleTuesday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleTuesday" to vm.macro.value!!.scheduleTuesday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleWednesday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleWednesday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleWednesday = true
                binding.chipScheduleWednesday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleWednesday" to vm.macro.value!!.scheduleWednesday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleThursday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleThursday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleThursday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleThursday = true
                binding.chipScheduleThursday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleThursday" to vm.macro.value!!.scheduleThursday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleFriday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleFriday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleFriday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleFriday = true
                binding.chipScheduleFriday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleFriday" to vm.macro.value!!.scheduleFriday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.chipScheduleSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleSaturday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleSaturday = isChecked
            if (!vm.macro.value!!.scheduleDay && !isChecked) {
                vm.macro.value!!.scheduleSaturday = true
                binding.chipScheduleSaturday.isChecked = true
                showNotUpdateNotification()
                return@setOnCheckedChangeListener
            }
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "scheduleSaturday" to vm.macro.value!!.scheduleSaturday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.buttonBackToMacroEdit.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showUpdateNotification() {
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        Snackbar.make(root, R.string.notification_update_macro, Snackbar.LENGTH_SHORT).show()
    }

    private fun showNotUpdateNotification() {
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        Snackbar.make(root, R.string.notification_not_update_macro, Snackbar.LENGTH_SHORT).show()
    }
}
