package inc.proto.websitemacrorecorder.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.databinding.FragmentListBinding
import java.util.*

class ListFragment : Fragment() {

    private lateinit var _binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = this

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.buttonAdd.setOnClickListener {
            val macro = Macro(
                acceptLanguage = Locale.getDefault().language,
                deviceScaleFactor = requireContext().resources.displayMetrics.density
            )
            val action = ListFragmentDirections.actionListFragmentToEditUrlFragment(macro)
            findNavController().navigate(action)
        }
    }
}
