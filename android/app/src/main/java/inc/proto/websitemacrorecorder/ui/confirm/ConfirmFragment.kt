package inc.proto.websitemacrorecorder.ui.confirm

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.FirebaseFunctionsException
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentConfirmBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.Helper.mapToObject
import inc.proto.websitemacrorecorder.util.Helper.objectToMap

class ConfirmFragment : Fragment() {
    private val vm: ConfirmViewModel by lazy {
        ViewModelProvider(this, ConfirmViewModelFactory(args.macro)).get(ConfirmViewModel::class.java)
    }
    private val args: ConfirmFragmentArgs by navArgs()
    private val macroRepository = MacroRepository()
    private var loading = false
    private lateinit var binding: FragmentConfirmBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_confirm, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                if (loading || activity == null) return false
                loading = true
                binding.progress.visibility = View.VISIBLE
                val macro = objectToMap(vm.macro.value!!)
                macroRepository.create(macro).addOnCompleteListener(requireActivity()) {
                    binding.progress.visibility = View.GONE
                    loading = false
                    if (!it.isSuccessful) {
                        if (activity == null) return@addOnCompleteListener
                        val exception = it.exception as FirebaseFunctionsException
                        val root: View = requireActivity().findViewById(R.id.root)
                        val text = when (exception.code) {
                            FirebaseFunctionsException.Code.INVALID_ARGUMENT -> root.resources.getString(R.string.error_invalid_argument)
                            FirebaseFunctionsException.Code.UNAUTHENTICATED -> root.resources.getString(R.string.error_unauthenticated)
                            FirebaseFunctionsException.Code.UNAVAILABLE -> root.resources.getString(R.string.error_unavailable)
                            FirebaseFunctionsException.Code.INTERNAL -> root.resources.getString(R.string.error_internal)
                            FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> root.resources.getString(R.string.error_deadline_exceeded)
                            else -> root.resources.getString(R.string.error_unknown)
                        }
                        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    val data = it.result!!.data as HashMap<String, Any?>
                    val action = ConfirmFragmentDirections.actionConfirmFragmentToEditFragment(mapToObject(data))
                    findNavController().navigate(action)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun bindViewModel() {
        Glide.with(this).load(vm.macro.value!!.screenshotUrl).into(binding.imageScreenshot)
    }
}
