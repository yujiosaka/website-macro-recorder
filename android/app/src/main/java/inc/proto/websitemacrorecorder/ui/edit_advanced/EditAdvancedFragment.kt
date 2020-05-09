package inc.proto.websitemacrorecorder.ui.edit_advanced

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R

class EditAdvancedFragment : Fragment() {

    private val _args: EditAdvancedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_advanced, container, false)
    }

}
