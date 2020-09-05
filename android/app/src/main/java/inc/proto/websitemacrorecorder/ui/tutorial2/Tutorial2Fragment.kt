package inc.proto.websitemacrorecorder.ui.tutorial2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentTutorial2Binding
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class Tutorial2Fragment : Fragment() {
    private lateinit var binding: FragmentTutorial2Binding
    private val vm: Tutorial2ViewModel by lazy {
        ViewModelProvider(this).get(Tutorial2ViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutorial2, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        bindViewModel()
    }

    private fun hideActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    private fun bindViewModel() {
        binding.buttonContinue.setOnSingleClickListener {
            findNavController().navigate(Tutorial2FragmentDirections.actionTutorial2FragmentToTutorial3Fragment())
        }
    }
}
