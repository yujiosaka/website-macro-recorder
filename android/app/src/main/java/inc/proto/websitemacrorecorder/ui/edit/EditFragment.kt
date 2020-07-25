package inc.proto.websitemacrorecorder.ui.edit

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestoreException
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.databinding.FragmentEditBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class EditFragment : Fragment() {
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

        binding.textBack.paintFlags = binding.textBack.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textBack.setOnSingleClickListener {
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
        }
        binding.textUrl.paintFlags = binding.textBack.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textUrl.setOnSingleClickListener {
            val action = EditFragmentDirections.actionEditFragmentToEditUrlFragment(vm.macro.value!!)
            findNavController().navigate(action)
        }
        binding.textSchedule.paintFlags = binding.textBack.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textSchedule.setOnSingleClickListener {
            val action = EditFragmentDirections.actionEditFragmentToEditScheduleFragment(vm.macro.value!!)
            findNavController().navigate(action)
        }
        binding.buttonDelete.setOnSingleClickListener {
            if (context == null) return@setOnSingleClickListener
            AlertDialog.Builder(context)
                .setMessage(resources.getString(R.string.message_delete))
                .setPositiveButton(R.string.message_yes) { _, _ ->
                    macroRepository.delete(vm.macro.value!!.id)
                    findNavController().navigate(R.id.action_editFragment_to_listFragment)
                }
                .setNegativeButton(R.string.message_no, null)
                .show()
        }
        binding.editName.doAfterTextChanged {
            vm.macro.value!!.name = it.toString()
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "name" to vm.macro.value!!.name,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }
        binding.editEnableSchedule.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.enableSchedule == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.enableSchedule = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "enableSchedule" to vm.macro.value!!.enableSchedule,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }
        binding.editNotifySuccess.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifySuccess == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.notifySuccess = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "notifySuccess" to vm.macro.value!!.notifySuccess,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }
        binding.editNotifyFailure.setOnCheckedChangeListener { _, isChecked ->
            if (vm.macro.value!!.notifyFailure == isChecked) return@setOnCheckedChangeListener
            vm.macro.value!!.notifyFailure = isChecked
            macroRepository.update(vm.macro.value!!.id, mapOf(
                "notifyFailure" to vm.macro.value!!.notifyFailure,
                "updatedAt" to FieldValue.serverTimestamp()
            ))
        }

        macroRepository.get(vm.macro.value!!.id).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                if (activity == null) return@addOnCompleteListener
                val exception = it.exception as FirebaseFirestoreException
                val root: View = requireActivity().findViewById(R.id.root)
                val text = when (exception.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> root.resources.getString(R.string.error_permission_denied)
                    else -> root.resources.getString(R.string.error_unknown)
                }
                Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
                return@addOnCompleteListener
            }
            val macro = Helper.mapToObject<Macro>(it.result!!.data!!)
            vm.resetMacro(macro)
        }
    }
}
