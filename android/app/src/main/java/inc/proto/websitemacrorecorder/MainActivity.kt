package inc.proto.websitemacrorecorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1

    private lateinit var _navController: NavController
    private lateinit var _firebaseAuth: FirebaseAuth
    private lateinit var _authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        _firebaseAuth = FirebaseAuth.getInstance()
        _authStateListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null) {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(listOf(GoogleBuilder().build(), EmailBuilder().build()))
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

        _navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(_navController.graph)
        setupActionBarWithNavController(_navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_plan -> {
                _navController.navigate(R.id.action_global_planFragment)
                true
            }
            R.id.action_signout -> {
                AuthUI.getInstance().signOut(this)
                _navController.navigate(R.id.action_global_listFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return _navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        _firebaseAuth.removeAuthStateListener(_authStateListener)
    }

    override fun onResume() {
        super.onResume()
        _firebaseAuth.addAuthStateListener(_authStateListener)
    }
}
