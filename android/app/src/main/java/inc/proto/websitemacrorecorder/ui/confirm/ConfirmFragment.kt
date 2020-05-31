package inc.proto.websitemacrorecorder.ui.confirm

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {
    private val _args: ConfirmFragmentArgs by navArgs()

    private lateinit var _binding: FragmentConfirmBinding
    private lateinit var _vm: ConfirmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val factory = ConfirmViewModelFactory(_args.macro)

        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        _vm = ViewModelProviders.of(this, factory).get(ConfirmViewModel::class.java)
        _binding.vm = _vm
        _binding.lifecycleOwner = this

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(_args.macro.screenshotUrl).into(_binding.screenshot);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_confirm, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val action = ConfirmFragmentDirections.actionConfirmFragmentToEditFragment(_args.macro)
                findNavController().navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}