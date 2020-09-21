package inc.proto.websitemacrorecorder.ui.tutorial1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.databinding.FragmentTutorial1Binding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener

@AndroidEntryPoint
class Tutorial1Fragment : BaseFragment() {
    private lateinit var binding: FragmentTutorial1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial1Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        setListeners()
    }

    private fun setListeners() {
        binding.buttonContinue.setOnSingleClickListener {
            findNavController().navigate(
                Tutorial1FragmentDirections.actionTutorial1FragmentToTutorial2Fragment()
            )
        }
    }
}
