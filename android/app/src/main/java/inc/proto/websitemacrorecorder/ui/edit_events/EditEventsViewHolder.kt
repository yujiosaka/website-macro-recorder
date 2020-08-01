package inc.proto.websitemacrorecorder.ui.edit_events

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event.view.*

class EditEventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layoutEvent: ConstraintLayout = view.layout_event
    var imageName: ImageView = view.image_name
    var textName: TextView = view.text_name
    var textTargetType: TextView = view.text_target_type
    var textValue: TextView = view.text_value
    var imageReorder: ImageView = view.image_reorder
}
