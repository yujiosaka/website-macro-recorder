package inc.proto.websitemacrorecorder.ui.view_histories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroHistory
import kotlinx.android.synthetic.main.fragment_view_histories.*

class ViewHistoriesFragment : Fragment() {
    private val adapter: ViewHistoriesAdapter by lazy {
        ViewHistoriesAdapter(args.macro.histories.reversed() as ArrayList<MacroHistory>)
    }
    private val args: ViewHistoriesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_histories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_histories.setHasFixedSize(true)
        recycler_histories.layoutManager = LinearLayoutManager(activity)
        recycler_histories.adapter = adapter
        setActionBarTitle()
    }

    private fun setActionBarTitle() {
        if (args.macro.name != "") {
            (activity as AppCompatActivity?)?.supportActionBar?.title = args.macro.name
        }
    }
}
