package inc.proto.websitemacrorecorder.ui.edit_selected_area

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditSelectedAreaBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.DragRectView


class EditSelectedAreaFragment : Fragment(), DragRectView.Listener {
    private val args: EditSelectedAreaFragmentArgs by navArgs()
    private val macroRepository = MacroRepository()

    private lateinit var binding: FragmentEditSelectedAreaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_selected_area, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_selected_area, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reset -> {
                args.macro.selectedAreaRect = null
                binding.dragRectangle.resetDraw()
                true
            }
            R.id.action_done -> {
                args.macro.checkSelectedArea = args.macro.checkSelectedArea && args.macro.selectedAreaRect != null
                macroRepository.update(args.macro.id, mapOf(
                    "selectedAreaLeft" to args.macro.selectedAreaLeft,
                    "selectedAreaTop" to args.macro.selectedAreaTop,
                    "selectedAreaRight" to args.macro.selectedAreaRight,
                    "selectedAreaBottom" to args.macro.selectedAreaBottom,
                    "checkSelectedArea" to args.macro.checkSelectedArea,
                    "updatedAt" to FieldValue.serverTimestamp()
                ))
                findNavController().popBackStack()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()

        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        val text = root.resources.getString(R.string.notification_select_area)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun bindViewModel() {
        Glide.with(this).load(args.macro.screenshotUrl).into(binding.imageScreenshot)
        binding.dragRectangle.setListener(this)
        if (args.macro.selectedAreaRect != null) {
            binding.dragRectangle.setRect(args.macro.selectedAreaRect!!)
        }
    }

    override fun startDraw() {
        binding.scrollEditSelectedArea.requestDisallowInterceptTouchEvent(true)
    }

    override fun endDraw(_rect: Rect) {
        binding.scrollEditSelectedArea.requestDisallowInterceptTouchEvent(false)
        args.macro.selectedAreaRect = _rect
    }
}
