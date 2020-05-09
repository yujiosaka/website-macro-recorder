package inc.proto.websitemacrorecorder.ui.edit_advanced

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditAdvancedBinding

class EditAdvancedFragment : Fragment() {

    private val _args: EditAdvancedFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditAdvancedBinding
    private lateinit var _vm: EditAdvancedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditAdvancedViewModelFactory(_args.macro)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_advanced, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditAdvancedViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }

}
