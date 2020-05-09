package inc.proto.websitemacrorecorder.ui.list

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import java.util.*

class ListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.button_add).setOnClickListener {
            val macro = Macro(
                userAgent = WebView(activity).settings.userAgentString,
                acceptLanguage = Locale.getDefault().getLanguage()
            )
            val action = ListFragmentDirections.actionListFragmentToEditUrlFragment(macro)
            findNavController().navigate(action)
        }
    }
}
