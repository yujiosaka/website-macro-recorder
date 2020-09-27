package inc.proto.websitemacrorecorder.ui.tutorial3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.databinding.FragmentTutorial3Binding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener

@AndroidEntryPoint
class Tutorial3Fragment : BaseFragment() {
    private val vm by viewModels<Tutorial3ViewModel>()
    private lateinit var binding: FragmentTutorial3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial3Binding.inflate(inflater, container, false)
        binding.vm = vm
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
                Tutorial3FragmentDirections.actionTutorial3FragmentToTutorial4Fragment()
            )
        }
    }
}
