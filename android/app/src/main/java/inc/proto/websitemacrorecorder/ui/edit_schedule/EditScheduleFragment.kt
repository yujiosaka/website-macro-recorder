package inc.proto.websitemacrorecorder.ui.edit_schedule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import inc.proto.websitemacrorecorder.R

class EditScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_schedule, container, false)
    }

}
