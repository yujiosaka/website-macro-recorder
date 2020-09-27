package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditScheduleBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.dialog.time_picker_dialog.TimePickerDialogFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener

@AndroidEntryPoint
class EditScheduleFragment : BaseFragment(), TimePickerDialogFragment.Listener {
    companion object {
        private const val DIALOG_TAG = "time_picker"
    }

    private val vm by viewModels<EditScheduleViewModel>()
    private lateinit var binding: FragmentEditScheduleBinding

    override fun onSelectedTime(hourOfDay: Int, minute: Int) {
        vm.macro.value!!.scheduleHour = hourOfDay
        vm.macro.value!!.scheduleMinute = minute

        vm.updateMacro(mapOf(
            "scheduleHour" to vm.macro.value!!.scheduleHour,
            "scheduleMinute" to vm.macro.value!!.scheduleMinute,
            "updatedAt" to FieldValue.serverTimestamp()
        ))

        notify(R.string.notification_update_macro)
    }

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

        setListeners()
    }

    private fun setListeners() {
        binding.editSchedule.setOnSingleClickListener {
            if (activity == null) return@setOnSingleClickListener
            val dialog = TimePickerDialogFragment(
                vm.macro.value!!.scheduleHour,
                vm.macro.value!!.scheduleMinute,
                this
            )
            dialog.show(requireActivity().supportFragmentManager, DIALOG_TAG)
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

                    vm.updateMacro(mapOf(
                        "scheduleFrequency" to vm.macro.value!!.scheduleFrequency,
                        "updatedAt" to FieldValue.serverTimestamp()
                    ))

                    notify(R.string.notification_update_macro)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.chipScheduleSunday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleSunday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleSunday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleSunday = true
                binding.chipScheduleSunday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleSunday" to vm.macro.value!!.scheduleSunday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleMonday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleMonday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleMonday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleMonday = true
                binding.chipScheduleMonday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleMonday" to vm.macro.value!!.scheduleMonday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleTuesday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleTuesday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleTuesday = true
                binding.chipScheduleTuesday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleTuesday" to vm.macro.value!!.scheduleTuesday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleWednesday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleWednesday = isChecked
            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleWednesday = true
                binding.chipScheduleWednesday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleWednesday" to vm.macro.value!!.scheduleWednesday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleThursday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleThursday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleThursday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleThursday = true
                binding.chipScheduleThursday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleThursday" to vm.macro.value!!.scheduleThursday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleFriday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleFriday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleFriday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleFriday = true
                binding.chipScheduleFriday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleFriday" to vm.macro.value!!.scheduleFriday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.chipScheduleSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.scheduleSaturday == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.scheduleSaturday = isChecked

            if (!vm.macro.value!!.isScheduled() && !isChecked) {
                vm.macro.value!!.scheduleSaturday = true
                binding.chipScheduleSaturday.isChecked = true
                notify(R.string.notification_not_update_macro)
                return@setOnCheckedChangeListener
            }

            vm.updateMacro(mapOf(
                "scheduleSaturday" to vm.macro.value!!.scheduleSaturday,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.buttonBackToMacroEdit.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }
}
