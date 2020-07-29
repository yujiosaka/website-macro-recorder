package inc.proto.websitemacrorecorder.ui.tutorial2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener
import kotlinx.android.synthetic.main.fragment_tutorial2.*

class Tutorial2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        Glide.with(this).load(R.raw.image_tutorial2).into(image_tutorial2)
        button_continue.setOnSingleClickListener {
            val action = Tutorial2FragmentDirections.actionTutorial2FragmentToTutorial3Fragment()
            findNavController().navigate(action)
        }
    }

    private fun hideActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }
}
