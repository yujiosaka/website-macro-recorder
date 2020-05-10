package inc.proto.websitemacrorecorder.ui.edit_events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs

import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditEventsBinding

class EditEventsFragment : Fragment() {

    private val _args: EditEventsFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditEventsBinding
    private lateinit var _vm: EditEventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditEventsViewModelFactory(_args.macro)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_events, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditEventsViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }
}
