package inc.proto.websitemacrorecorder.ui.edit

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper.throttle
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class EditFragment : Fragment() {
    companion object {
        private const val THROTTLE_WAIT = 500L
    }

    private val vm: EditViewModel by lazy {
        ViewModelProvider(this, EditViewModelFactory(args.macro)).get(EditViewModel::class.java)
    }
    private val args: EditFragmentArgs by navArgs()
    private val macroRepository = MacroRepository()
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
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.textUrl.paintFlags = binding.textUrl.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textUrl.setOnSingleClickListener {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToEditUrlFragment(vm.macro.value!!))
        }
        binding.textSchedule.paintFlags = binding.textSchedule.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textSchedule.setOnSingleClickListener {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToEditScheduleFragment(vm.macro.value!!))
        }
        binding.buttonDelete.setOnSingleClickListener {
            AlertDialog.Builder(context)
                .setMessage(resources.getString(R.string.message_delete))
                .setPositiveButton(R.string.message_yes) { _, _ ->
                    macroRepository.delete(vm.macro.value!!.id)
                    findNavController().navigate(R.id.action_editFragment_to_listFragment)
                }
                .setNegativeButton(R.string.message_no, null)
                .show()
        }
        val doAfterTextChangedThrottled = throttle<Editable?>(THROTTLE_WAIT) {
            if (it == null) return@throttle
            if (vm.macro.value!!.name == it.toString()) return@throttle
            vm.macro.value!!.name = it.toString()
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "name" to vm.macro.value!!.name,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.editName.doAfterTextChanged {
            doAfterTextChangedThrottled(it)
        }
        binding.editEnableSchedule.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.enableSchedule == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.enableSchedule = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "enableSchedule" to vm.macro.value!!.enableSchedule,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.editNotifySuccess.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifySuccess == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.notifySuccess = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "notifySuccess" to vm.macro.value!!.notifySuccess,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.editNotifyFailure.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifyFailure == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.notifyFailure = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "notifyFailure" to vm.macro.value!!.notifyFailure,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.editCheckEntirePage.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.checkEntirePage == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.checkEntirePage = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "checkEntirePage" to vm.macro.value!!.checkEntirePage,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.editCheckSelectedArea.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.checkSelectedArea == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.checkSelectedArea = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "checkSelectedArea" to vm.macro.value!!.checkSelectedArea,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
            showUpdateNotification()
        }
        binding.textCheckSelectedArea.paintFlags = binding.textCheckSelectedArea.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textCheckSelectedArea.setOnSingleClickListener {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToEditSelectedAreaFragment(vm.macro.value!!))
        }
        binding.buttonBackToMacroList.setOnSingleClickListener {
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
        }
    }

    private fun showUpdateNotification() {
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        Snackbar.make(root, R.string.notification_update_macro, Snackbar.LENGTH_SHORT).show()
    }
}
