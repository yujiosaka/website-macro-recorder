package inc.proto.websitemacrorecorder.ui.edit_url

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.buttonStartRecording.setOnSingleClickListener {
            val action = EditUrlFragmentDirections.actionEditUrlFragmentToEditRecordFragment(vm.macro.value!!)
            findNavController().navigate(action)
        }
    }
}
