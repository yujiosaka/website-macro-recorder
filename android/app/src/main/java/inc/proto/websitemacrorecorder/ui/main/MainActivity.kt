package inc.proto.websitemacrorecorder.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.NavGraphDirections
import inc.proto.websitemacrorecorder.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 1
    }

    private val vm by viewModels<MainViewModel>()
    private val authStateListener: FirebaseAuth.AuthStateListener by lazy {
        FirebaseAuth.AuthStateListener {
            if (it.currentUser != null) return@AuthStateListener
            val intent = authUI
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(GoogleBuilder().build(), EmailBuilder().build()))
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    @Inject
    lateinit var authUI: AuthUI

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))

        if (intent.action != Intent.ACTION_SEND) return

        val macro = vm.buildMacro(intent.getStringExtra(Intent.EXTRA_TEXT))
        navController.navigate(NavGraphDirections.actionGlobalEditUrlFragment(macro))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_plan -> goToPlan()
            R.id.action_signout -> signOut()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != RC_SIGN_IN || resultCode != Activity.RESULT_CANCELED) return
        finish()
    }

    override fun onPause() {
        super.onPause()

        vm.removeAuthStateListener(authStateListener)
    }

    override fun onResume() {
        super.onResume()

        vm.addAuthStateListener(authStateListener)
    }

    private fun goToPlan(): Boolean {
        navController.navigate(R.id.action_global_planFragment)

        return true
    }

    private fun signOut(): Boolean {
        authUI.signOut(this)
        navController.navigate(R.id.action_global_listFragment)

        return true
    }
}
