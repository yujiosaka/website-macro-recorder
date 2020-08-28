package inc.proto.websitemacrorecorder.ui.edit_events

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event.view.*

class EditEventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageName: ImageView = view.image_name
    val textName: TextView = view.text_name
    val textTargetType: TextView = view.text_target_type
    val textValue: TextView = view.text_value
    val imageWarning: ImageView = view.image_warning
    val textMessage: TextView = view.text_message
    val imageReorder: ImageView = view.image_reorder
}
