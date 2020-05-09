package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.R

class EditScheduleFragment : Fragment() {

    private val _args: EditScheduleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_schedule, container, false)
    }

}
