package inc.proto.websitemacrorecorder.ui.show_history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.DISABLED_ALPHA
import inc.proto.websitemacrorecorder.ENABLED_ALPHA
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentShowHistoryBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.util.TextFormatter

@AndroidEntryPoint
class ShowHistoryFragment(private val textFormatter: TextFormatter) : BaseFragment() {
    private val vm by viewModels<ShowHistoryViewModel>()
    private lateinit var binding: FragmentShowHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentShowHistoryBinding.inflate(inflater, container, false)
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

        preparePreviousMenuItem(menu)
        prepareNextMenuItem(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_previous -> goPrevious()
            R.id.action_next -> goNext()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle(textFormatter.dateToDateTime(vm.history.value!!.executedAt!!.toDate()))
    }

    private fun preparePreviousMenuItem(menu: Menu) {
        val index = vm.macro.value!!.histories.indexOf(vm.history.value!!)
        val previousItem = menu.findItem(R.id.action_previous)
        if (index == 0) {
            previousItem.isEnabled = false
            previousItem.icon.alpha = DISABLED_ALPHA
        } else {
            previousItem.isEnabled = true
            previousItem.icon.alpha = ENABLED_ALPHA
        }
    }

    private fun prepareNextMenuItem(menu: Menu) {
        val index = vm.macro.value!!.histories.indexOf(vm.history.value!!)
        val nextItem = menu.findItem(R.id.action_next)
        if (index == vm.macro.value!!.histories.size - 1) {
            nextItem.isEnabled = false
            nextItem.icon.alpha = DISABLED_ALPHA
        } else {
            nextItem.isEnabled = true
            nextItem.icon.alpha = ENABLED_ALPHA
        }
    }

    private fun goPrevious(): Boolean {
        val macro = vm.macro.value!!
        val index = macro.histories.indexOf(vm.history.value!!)
        if (index == 0) return false

        val history = macro.histories[index - 1]
        findNavController().navigate(
            ShowHistoryFragmentDirections.actionViewHistoryFragmentToViewHistoryFragmentPrevious(macro, history)
        )
        return true
    }

    private fun goNext(): Boolean {
        val macro = vm.macro.value!!
        val index = macro.histories.indexOf(vm.history.value!!)
        if (index == macro.histories.size - 1) return false

        val history = macro.histories[index + 1]
        findNavController().navigate(
            ShowHistoryFragmentDirections.actionViewHistoryFragmentToViewHistoryFragmentNext(macro, history)
        )
        return true
    }
}
