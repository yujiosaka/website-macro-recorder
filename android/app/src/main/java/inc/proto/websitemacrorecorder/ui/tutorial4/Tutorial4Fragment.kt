package inc.proto.websitemacrorecorder.ui.tutorial4

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentTutorial4Binding
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class Tutorial4Fragment : Fragment() {
    private lateinit var binding: FragmentTutorial4Binding

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutorial4, container, false)
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
        Glide.with(this).load(R.raw.image_tutorial4).into(binding.imageTutorial4)
        binding.buttonStart.setOnSingleClickListener {
            sharedPreferences.edit { putBoolean("VIEWED_TUTORIAL", true) }
            findNavController().navigate(Tutorial4FragmentDirections.actionTutorial4FragmentToListFragmentWithoutHistory())
        }
    }
}
