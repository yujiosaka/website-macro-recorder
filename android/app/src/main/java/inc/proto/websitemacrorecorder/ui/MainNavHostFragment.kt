package inc.proto.websitemacrorecorder.ui

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.factory.AppFragmentFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment() {
    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        childFragmentManager.fragmentFactory = fragmentFactory
    }
}
