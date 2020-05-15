package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.databinding.FragmentEditScheduleBinding

class EditScheduleFragment : Fragment() {

    private val _args: EditScheduleFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditScheduleBinding
    private lateinit var _vm: EditScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditScheduleViewModelFactory(_args.macro)

        _binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditScheduleViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this

        return _binding.root
    }

}
