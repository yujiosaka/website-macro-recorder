package inc.proto.websitemacrorecorder.ui.edit

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener
import inc.proto.websitemacrorecorder.util.Helper

@AndroidEntryPoint
class EditFragment(private val helper: Helper) : BaseFragment() {
    companion object {
        private const val THROTTLE_WAIT = 500L
    }

    private val vm by viewModels<EditViewModel>()
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.textUrl.setOnSingleClickListener {
            findNavController().navigate(
                EditFragmentDirections.actionEditFragmentToEditUrlFragment(vm.macro.value!!)
            )
        }
        binding.textSchedule.setOnSingleClickListener {
            findNavController().navigate(
                EditFragmentDirections.actionEditFragmentToEditScheduleFragment(vm.macro.value!!)
            )
        }
        binding.buttonDelete.setOnSingleClickListener {
            showAlertDialog()
        }
        val doAfterTextChangedThrottled = helper.throttle<Editable?>(THROTTLE_WAIT) {
            if (it == null) return@throttle
            if (vm.macro.value!!.name == it.toString()) return@throttle

            vm.macro.value!!.name = it.toString()
            vm.updateMacro(mapOf(
                "name" to vm.macro.value!!.name,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.editName.doAfterTextChanged {
            doAfterTextChangedThrottled(it)
        }
        binding.editEnableSchedule.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.enableSchedule == isChecked) return@setOnCheckedChangeListener

            vm.macro.value!!.enableSchedule = isChecked
            vm.updateMacro(mapOf(
                "enableSchedule" to vm.macro.value!!.enableSchedule,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.editNotifySuccess.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifySuccess == isChecked) return@setOnCheckedChangeListener

            vm.macro.value!!.notifySuccess = isChecked
            vm.updateMacro(mapOf(
                "notifySuccess" to vm.macro.value!!.notifySuccess,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.editNotifyFailure.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifyFailure == isChecked) return@setOnCheckedChangeListener

            vm.macro.value!!.notifyFailure = isChecked
            vm.updateMacro(mapOf(
                "notifyFailure" to vm.macro.value!!.notifyFailure,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.editCheckEntirePage.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.checkEntirePage == isChecked) return@setOnCheckedChangeListener

            vm.macro.value!!.checkEntirePage = isChecked
            vm.updateMacro(mapOf(
                "checkEntirePage" to vm.macro.value!!.checkEntirePage,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.editCheckSelectedArea.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.checkSelectedArea == isChecked) return@setOnCheckedChangeListener

            vm.macro.value!!.checkSelectedArea = isChecked
            vm.updateMacro(mapOf(
                "checkSelectedArea" to vm.macro.value!!.checkSelectedArea,
                "updatedAt" to FieldValue.serverTimestamp()
            ))

            notify(R.string.notification_update_macro)
        }
        binding.textCheckSelectedArea.setOnSingleClickListener {
            findNavController().navigate(
                EditFragmentDirections.actionEditFragmentToEditSelectedAreaFragment(vm.macro.value!!)
            )
        }
        binding.buttonBackToMacroList.setOnSingleClickListener {
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
        }
    }

    private fun showAlertDialog() {
        if (context == null) return
        AlertDialog.Builder(requireContext())
            .setMessage(resources.getString(R.string.message_delete))
            .setPositiveButton(R.string.message_yes) { _, _ ->
                vm.deleteMacro()
                findNavController().navigate(R.id.action_editFragment_to_listFragment)
            }
            .setNegativeButton(R.string.message_no, null)
            .show()
    }
}
