package inc.proto.websitemacrorecorder.ui.confirm

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.functions.FirebaseFunctionsException
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentConfirmBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.util.TextFormatter

@AndroidEntryPoint
class ConfirmFragment(
    private val textFormatter: TextFormatter,
    private val helper: Helper
) : BaseFragment() {
    private val vm by viewModels<ConfirmViewModel>()
    private lateinit var binding: FragmentConfirmBinding

    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentConfirmBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_confirm, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> return save()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save(): Boolean {
        if (loading || activity == null) return false

        loading = true
        binding.progress.visibility = View.VISIBLE

        vm.createMacro().addOnCompleteListener(requireActivity()) {
            binding.progress.visibility = View.GONE
            loading = false

            if (!it.isSuccessful) {
                val exception = it.exception as FirebaseFunctionsException
                notify(textFormatter.firebaseFunctionExceptionToMessage(exception))
                return@addOnCompleteListener
            }

            findNavController().navigate(
                ConfirmFragmentDirections.actionConfirmFragmentToEditFragment(
                    helper.mapToObject(it.result!!.data)
                )
            )
        }
        return true
    }
}
