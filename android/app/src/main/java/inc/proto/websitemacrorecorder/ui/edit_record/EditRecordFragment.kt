package inc.proto.websitemacrorecorder.ui.edit_record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditRecordBinding

class EditRecordFragment : Fragment() {

    private val _args: EditRecordFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditRecordBinding
    private lateinit var _vm: EditRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditRecordViewModelFactory(_args.macro)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_record, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditRecordViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }

}
