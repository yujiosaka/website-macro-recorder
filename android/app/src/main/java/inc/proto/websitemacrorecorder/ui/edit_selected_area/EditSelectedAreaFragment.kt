package inc.proto.websitemacrorecorder.ui.edit_selected_area

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.databinding.FragmentEditSelectedAreaBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.ui.view.DragRectView

@AndroidEntryPoint
class EditSelectedAreaFragment : BaseFragment(), DragRectView.Listener {
    private lateinit var binding: FragmentEditSelectedAreaBinding
    private val vm by viewModels<EditSelectedAreaViewModel>()

    override fun startDraw() {
        binding.scrollEditSelectedArea.requestDisallowInterceptTouchEvent(true)
    }

    override fun endDraw(rect: Rect) {
        binding.scrollEditSelectedArea.requestDisallowInterceptTouchEvent(false)
        vm.macro.value!!.setSelectedAreaRect(rect)
        notify(R.string.notification_area_selected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentEditSelectedAreaBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_selected_area, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reset -> reset()
            R.id.action_done -> save()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        notify(resources.getString(R.string.notification_select_area))
    }

    private fun bindViewModel() {
        binding.dragRectangle.setListener(this)
        if (!vm.macro.value!!.isAreaSelected()) return

        binding.dragRectangle.setRect(vm.macro.value!!.getSelectedAreaRect()!!)
    }

    private fun save(): Boolean {
        vm.macro.value!!.checkSelectedArea = vm.macro.value!!.checkSelectedArea && vm.macro.value!!.isAreaSelected()

        vm.updateMacro(mapOf(
            "selectedAreaLeft" to vm.macro.value!!.selectedAreaLeft,
            "selectedAreaTop" to vm.macro.value!!.selectedAreaTop,
            "selectedAreaRight" to vm.macro.value!!.selectedAreaRight,
            "selectedAreaBottom" to vm.macro.value!!.selectedAreaBottom,
            "checkSelectedArea" to vm.macro.value!!.checkSelectedArea,
            "updatedAt" to FieldValue.serverTimestamp()
        ))

        findNavController().popBackStack()
        return true
    }

    private fun reset(): Boolean {
        vm.macro.value!!.setSelectedAreaRect(null)
        binding.dragRectangle.resetDraw()

        return true
    }
}
