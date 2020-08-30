package inc.proto.websitemacrorecorder.ui.view_histories

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_history.view.*

class ViewHistoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageScreenshot: ImageView = view.image_screenshot
    val textDate: TextView = view.text_date
    val chipChange: Chip = view.chip_change
    val chipError: Chip = view.chip_error
    val chipSuccess: Chip = view.chip_success
    val chipCheckScreenshot: Chip = view.chip_check_screenshot
}
