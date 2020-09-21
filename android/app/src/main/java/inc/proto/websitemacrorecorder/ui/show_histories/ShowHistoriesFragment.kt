package inc.proto.websitemacrorecorder.ui.show_histories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.databinding.FragmentShowHistoriesBinding
import inc.proto.websitemacrorecorder.factory.AppAdapterFactory
import inc.proto.websitemacrorecorder.factory.ViewHistoriesAdapterArgs
import inc.proto.websitemacrorecorder.ui.BaseFragment

@AndroidEntryPoint
class ShowHistoriesFragment(adapterFactory: AppAdapterFactory) : BaseFragment(), ShowHistoriesAdapter.Listener {
    private val vm by viewModels<ShowHistoriesViewModel>()
    private val adapter by lazy {
        var histories = if (vm.macro.value!!.histories.size >= 1) {
            vm.macro.value!!.histories.reversed() as ArrayList
        } else {
            ArrayList()
        }
        val args = ViewHistoriesAdapterArgs(histories, this)
        adapterFactory.instantiate(ShowHistoriesAdapter::class.java.name, args) as ShowHistoriesAdapter
    }
    private lateinit var binding: FragmentShowHistoriesBinding

    override fun onViewHistory(history: MacroHistory) {
        findNavController().navigate(
            ShowHistoriesFragmentDirections.actionViewHistoriesFragmentToViewHistoryFragment(vm.macro.value!!, history)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowHistoriesBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setActionBarTitle(getTitle())
    }

    private fun setAdapter() {
        binding.recyclerHistories.setHasFixedSize(true)
        binding.recyclerHistories.layoutManager = LinearLayoutManager(activity)
        binding.recyclerHistories.adapter = adapter
    }

    private fun getTitle(): String {
        return if (vm.macro.value!!.name != "") {
            vm.macro.value!!.name
        } else {
            resources.getString(R.string.text_no_name)
        }
    }
}
