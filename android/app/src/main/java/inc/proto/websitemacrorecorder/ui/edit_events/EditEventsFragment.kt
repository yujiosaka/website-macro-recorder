package inc.proto.websitemacrorecorder.ui.edit_events

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.functions.FirebaseFunctionsException
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.databinding.FragmentEditEventsBinding
import inc.proto.websitemacrorecorder.factory.AppAdapterFactory
import inc.proto.websitemacrorecorder.factory.AppCallbackFactory
import inc.proto.websitemacrorecorder.factory.EditEventsAdapterArgs
import inc.proto.websitemacrorecorder.factory.EditEventsCallbackArgs
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.dialog.edit_events_dialog.EditEventsDialog
import inc.proto.websitemacrorecorder.util.TextFormatter

@AndroidEntryPoint
class EditEventsFragment(
    appAdapterFactory: AppAdapterFactory,
    appCallbackFactory: AppCallbackFactory,
    private val textFormatter: TextFormatter
) : BaseFragment(), EditEventsAdapter.Listener, EditEventsDialog.Listener {
    companion object {
        private const val DIALOG_TAG = "add_timer_dialog"
        private val PATTERN = "^Timeout error occurred position: (\\d+)$".toRegex()
    }

    private val vm by viewModels<EditEventsViewModel>()
    private val adapter by lazy {
        val args = EditEventsAdapterArgs(vm.macro.value!!.events, this)
        appAdapterFactory.instantiate(EditEventsAdapter::class.java.name, args) as EditEventsAdapter
    }
    private val itemTouchHelper by lazy {
        val args = EditEventsCallbackArgs(0, ItemTouchHelper.RIGHT, vm, adapter)
        val callback = appCallbackFactory.instantiate(EditEventsCallback::class.java.name, args) as EditEventsCallback
        ItemTouchHelper(callback)
    }
    private lateinit var binding: FragmentEditEventsBinding

    private var loading = false

    override fun onStartDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onAddTimer(value: String) {
        adapter.addItem(MacroEvent(name = "timer", value = value))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentEditEventsBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerEvents.setHasFixedSize(true)
        binding.recyclerEvents.layoutManager = LinearLayoutManager(activity)
        binding.recyclerEvents.adapter = adapter

        itemTouchHelper.attachToRecyclerView(binding.recyclerEvents)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_events, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_timer -> addTimer()
            R.id.action_done -> screenshot()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addTimer(): Boolean {
        val dialog = EditEventsDialog(this)
        dialog.show(childFragmentManager, DIALOG_TAG)

        return true
    }

    private fun screenshot(): Boolean {
        if (loading || activity == null) return false

        loading = true
        binding.progress.visibility = View.VISIBLE

        itemTouchHelper.attachToRecyclerView(null)

        vm.screenshot().addOnCompleteListener(requireActivity()) {
            binding.progress.visibility = View.GONE
            loading = false

            itemTouchHelper.attachToRecyclerView(binding.recyclerEvents)

            if (!it.isSuccessful) {
                val exception = it.exception as FirebaseFunctionsException
                notify(textFormatter.firebaseFunctionExceptionToMessage(exception))

                val result = PATTERN.find(exception.message.toString())
                if (result != null) {
                    val (position) = result.destructured
                    val message = resources.getString(R.string.error_timeout)
                    adapter.setMessage(position.toInt(), message)
                }

                return@addOnCompleteListener
            }

            vm.macro.value!!.screenshotUrl = it.result!!.data as String
            findNavController().navigate(
                EditEventsFragmentDirections.actionEditEventsFragmentToConfirmFragment(vm.macro.value!!)
            )
        }
        return true
    }
}
