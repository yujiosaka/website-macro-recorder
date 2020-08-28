package inc.proto.websitemacrorecorder.ui.list

import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_macro.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageScreenshot: ImageView = view.image_screenshot
    val textSchedule: TextView = view.text_schedule
    val editEnableSchedule: Switch = view.edit_enable_schedule
    val textName: TextView = view.text_name
    val textUrl: TextView = view.text_url
    val textDate: TextView = view.text_date
    val chipError: Chip = view.chip_error
    val chipChange: Chip = view.chip_change
    val imagePlay: ImageView = view.image_play
    val imageMore: ImageView = view.image_more
}
