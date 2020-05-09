package inc.proto.websitemacrorecorder.ui.edit_url

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditUrlBinding

class EditUrlFragment : Fragment() {

    private val _args: EditUrlFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditUrlBinding
    private lateinit var _vm: EditUrlViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val factory = EditUrlViewModelFactory(_args.macro)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_url, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditUrlViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.buttonStartRecording.setOnClickListener {
            val action = EditUrlFragmentDirections.actionEditUrlFragmentToEditRecordFragment(_vm.macro)
            findNavController().navigate(action)
        }
    }
}
