package inc.proto.websitemacrorecorder.ui.edit

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditBinding

class EditFragment : Fragment() {

    private val _args: EditFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditBinding
    private lateinit var _vm: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditViewModelFactory(_args.macro)

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.textBack.setPaintFlags(_binding.textBack.paintFlags or Paint.UNDERLINE_TEXT_FLAG)
        _binding.textBack.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
        }
        _binding.textUrl.setPaintFlags(_binding.textUrl.paintFlags or Paint.UNDERLINE_TEXT_FLAG)
        _binding.textUrl.setOnClickListener {
            val action = EditFragmentDirections.actionEditFragmentToEditUrlFragment(_args.macro)
            findNavController().navigate(action)
        }
        _binding.textSchedule.setPaintFlags(_binding.textSchedule.paintFlags or Paint.UNDERLINE_TEXT_FLAG)
        val schedule = resources.getStringArray(R.array.text_frequency_array)[_vm.scheduleFrequency]
        _binding.textSchedule.text = if (_vm.scheduleFrequency == 1) {
            resources.getString(R.string.text_schedule, schedule, _vm.schedule)
        } else {
            schedule
        }
        _binding.textSchedule.setOnClickListener {
            val action = EditFragmentDirections.actionEditFragmentToEditScheduleFragment(_args.macro)
            findNavController().navigate(action)
        }
    }

}
