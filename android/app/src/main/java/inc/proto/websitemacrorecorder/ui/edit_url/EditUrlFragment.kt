package inc.proto.websitemacrorecorder.ui.edit_url

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.databinding.FragmentEditUrlBinding
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class EditUrlFragment : Fragment() {
    private val vm: EditUrlViewModel by lazy {
        ViewModelProvider(this, EditUrlViewModelFactory(args.macro)).get(EditUrlViewModel::class.java)
    }
    private val args: EditUrlFragmentArgs by navArgs()
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
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.editUrl.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_NULL &&
                event.action == KeyEvent.ACTION_DOWN &&
                binding.editUrl.error == null) {
                binding.buttonStartRecording.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.buttonStartRecording.setOnSingleClickListener {
            findNavController().navigate(EditUrlFragmentDirections.actionEditUrlFragmentToEditRecordFragment(vm.macro.value!!))
        }
    }
}
