package inc.proto.websitemacrorecorder.ui.edit_events

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event.view.*

class EditEventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageName: ImageView = view.image_name
    var textName: TextView = view.text_name
    var textTargetType: TextView = view.text_target_type
    var textValue: TextView = view.text_value
    var imageWarning: ImageView = view.image_warning
    var textMessage: TextView = view.text_message
    var imageReorder: ImageView = view.image_reorder
}
