package inc.proto.websitemacrorecorder.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import inc.proto.websitemacrorecorder.databinding.FragmentPlanBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment

class PlanFragment : BaseFragment() {
    private lateinit var binding: FragmentPlanBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }
}
