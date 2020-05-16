package inc.proto.websitemacrorecorder.ui.edit_events

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_event.view.*

class EditEventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageName: ImageView = view.image_name
    var textName: TextView = view.text_name_title
    var textTargetType: TextView = view.text_target_type
    var textValue: TextView = view.text_value
    var imageReorder: ImageView = view.image_reorder
}
