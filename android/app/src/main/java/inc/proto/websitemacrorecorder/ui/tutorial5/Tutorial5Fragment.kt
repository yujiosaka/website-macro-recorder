package inc.proto.websitemacrorecorder.ui.tutorial5

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.databinding.FragmentTutorial5Binding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener

@AndroidEntryPoint
class Tutorial5Fragment(private val sharedPreferences: SharedPreferences) : BaseFragment() {
    private val vm by viewModels<Tutorial5ViewModel>()
    private lateinit var binding: FragmentTutorial5Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial5Binding.inflate(inflater, container, false)
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
        binding.buttonStart.setOnSingleClickListener {
            sharedPreferences.edit { putBoolean("VIEWED_TUTORIAL", true) }
            findNavController().navigate(
                Tutorial5FragmentDirections .actionTutorial5FragmentToListFragmentWithoutHistory()
            )
        }
    }
}
