package inc.proto.websitemacrorecorder.ui.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditBinding

class EditFragment : Fragment() {

    private val _args: EditFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditBinding
    private lateinit var _vm: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditViewModelFactory(_args.macro)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }

}
