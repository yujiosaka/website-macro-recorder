package inc.proto.websitemacrorecorder.ui.tutorial1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener
import kotlinx.android.synthetic.main.fragment_tutorial1.*

class Tutorial1Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        button_continue.setOnSingleClickListener {
            val action = Tutorial1FragmentDirections.actionTutorial1FragmentToTutorial2Fragment()
            findNavController().navigate(action)
        }
    }

    private fun hideActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }
}
