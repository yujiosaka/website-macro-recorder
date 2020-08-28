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
    var card: CardView = view.card
    var imageScreenshot: ImageView = view.image_screenshot
    var textSchedule: TextView = view.text_schedule
    var editEnableSchedule: Switch = view.edit_enable_schedule
    var textName: TextView = view.text_name
    var textUrl: TextView = view.text_url
    var textDate: TextView = view.text_date
    var chipError: Chip = view.chip_error
    var chipChange: Chip = view.chip_change
}
