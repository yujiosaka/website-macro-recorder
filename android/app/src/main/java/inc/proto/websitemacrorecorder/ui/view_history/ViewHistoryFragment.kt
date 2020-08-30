package inc.proto.websitemacrorecorder.ui.view_history

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import inc.proto.websitemacrorecorder.DISABLED_ALPHA
import inc.proto.websitemacrorecorder.ENABLED_ALPHA
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentViewHistoryBinding

class ViewHistoryFragment : Fragment() {
    private val vm: ViewHistoryViewModel by lazy {
        val factory = ViewHistoryViewModelFactory(args.macro, args.history)
        ViewModelProvider(this, factory).get(ViewHistoryViewModel::class.java)
    }
    private val args: ViewHistoryFragmentArgs by navArgs()
    private lateinit var binding: FragmentViewHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentViewHistoryBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_view_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val index = args.macro.histories.indexOf(args.history)
        val previousItem = menu.findItem(R.id.action_previous)
        if (index == 0) {
            previousItem.isEnabled = false
            previousItem.icon.alpha = DISABLED_ALPHA
        } else {
            previousItem.isEnabled = true
            previousItem.icon.alpha = ENABLED_ALPHA
        }
        val nextItem = menu.findItem(R.id.action_next)
        if (index == args.macro.histories.size - 1) {
            nextItem.isEnabled = false
            nextItem.icon.alpha = DISABLED_ALPHA
        } else {
            nextItem.isEnabled = true
            nextItem.icon.alpha = ENABLED_ALPHA
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_previous -> {
                val index = args.macro.histories.indexOf(args.history)
                if (index == 0) return false
                val history = args.macro.histories[index - 1]
                val action = ViewHistoryFragmentDirections
                    .actionViewHistoryFragmentToViewHistoryFragmentPrevious(args.macro, history)
                findNavController().navigate(action)
                true
            }
            R.id.action_next -> {
                val index = args.macro.histories.indexOf(args.history)
                if (index == args.macro.histories.size - 1) return false
                val history = args.macro.histories[index + 1]
                val action = ViewHistoryFragmentDirections
                    .actionViewHistoryFragmentToViewHistoryFragmentNext(args.macro, history)
                findNavController().navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle()
    }

    private fun setActionBarTitle() {
        val date = DateFormat.getMediumDateFormat(context).format(args.history.executedAt!!.toDate())
        val time = DateFormat.getTimeFormat(context).format(args.history.executedAt!!.toDate())
        (activity as AppCompatActivity?)?.supportActionBar?.title = "$date $time"
    }
}
