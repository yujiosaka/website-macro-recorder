package inc.proto.websitemacrorecorder.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentPlanBinding

class PlanFragment : Fragment() {

    private lateinit var _binding: FragmentPlanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        _binding.lifecycleOwner = this
        return _binding.root
    }

}
