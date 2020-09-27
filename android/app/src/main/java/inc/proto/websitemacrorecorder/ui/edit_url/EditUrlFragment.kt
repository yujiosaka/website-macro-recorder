package inc.proto.websitemacrorecorder.ui.edit_url

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.databinding.FragmentEditUrlBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener

@AndroidEntryPoint
class EditUrlFragment : BaseFragment() {
    private val vm by viewModels<EditUrlViewModel>()
    private lateinit var binding: FragmentEditUrlBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUrlBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.editUrl.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_NULL && event.action == KeyEvent.ACTION_DOWN && vm.isValid.value!!) {
                binding.buttonStartRecording.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.buttonStartRecording.setOnSingleClickListener {
            findNavController().navigate(
                EditUrlFragmentDirections.actionEditUrlFragmentToEditRecordFragment(vm.macro.value!!)
            )
        }
    }
}
