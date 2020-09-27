package inc.proto.websitemacrorecorder.ui

import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import inc.proto.websitemacrorecorder.R

open class BaseFragment : Fragment() {
    protected fun notify(text: String) {
        if (activity == null) return

        val root: View = requireActivity().findViewById(R.id.root)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    protected fun notify(id: Int) {
        notify(resources.getString(id))
    }

    protected fun showActionBar() {
        getSupportActionBar()?.show()
    }

    protected fun hideActionBar() {
        getSupportActionBar()?.hide()
    }

    protected fun setActionBarTitle(title: String) {
        getSupportActionBar()?.title = title
    }

    private fun getSupportActionBar(): ActionBar? {
        val appCompactActivity = activity as AppCompatActivity?
        return appCompactActivity?.supportActionBar
    }
}
