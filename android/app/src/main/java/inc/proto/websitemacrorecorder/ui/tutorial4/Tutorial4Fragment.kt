package inc.proto.websitemacrorecorder.ui.tutorial4

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener
import kotlinx.android.synthetic.main.fragment_tutorial4.*

class Tutorial4Fragment : Fragment() {
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        Glide.with(this).load(R.raw.image_tutorial4).into(image_tutorial4)
        button_start.setOnSingleClickListener {
            sharedPreferences.edit { putBoolean("VIEWED_TUTORIAL", true) }
            val action = Tutorial4FragmentDirections.actionTutorial4FragmentToListFragmentWithoutHistory()
            findNavController().navigate(action)
        }
    }

    private fun hideActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }
}
