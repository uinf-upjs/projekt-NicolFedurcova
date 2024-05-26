package sk.upjs.hackstock

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import sk.upjs.hackstock.databinding.ActivityMainBinding
import sk.upjs.hackstock.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels{
        LoginViewModel.LoginViewModelFactory((application as MainApplication).repository)
    }
    private var logoutMenuItem: MenuItem? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = (application as MainApplication).getSharedPreferences()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_ai_tips,
                R.id.navigation_game,
                R.id.navigation_quiz,
                R.id.loginFragment,
                R.id.detailFragment

            )
        )
        //setSupportActionBar(binding.toolbar)
        actionBar?.elevation = 0F
        supportActionBar?.elevation=0F

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val userEmail = getUserEmailFromPreferences()
        if (userEmail != null) {
            // User is logged in, navigate to the home screen
            navigateToHomeScreen()
        } else {
            // User is not logged in, show the login screen
            navigateToLoginScreen()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            invalidateOptionsMenu()
            checkUserStatus()
        }

        checkUserStatus()

    }

    private fun getUserEmailFromPreferences(): String? {
        // Retrieve the user's email from SharedPreferences
        return getSharedPreferences(MainApplication.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(MainApplication.USER_EMAIL_KEY, null)
    }

    private fun navigateToHomeScreen() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.action_loginFragment_to_fragment_home)
    }

    private fun navigateToLoginScreen() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.loginFragment)
    }

    private fun checkUserStatus() {
        val userEmail = sharedPreferences.getString(MainApplication.USER_EMAIL_KEY, null)
        val navView: BottomNavigationView = binding.navView

        if (userEmail.isNullOrEmpty()) {
            navView.visibility = View.GONE
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            if (navController.currentDestination?.id != R.id.loginFragment) {
                navController.navigate(R.id.loginFragment)
            }
        } else {
            navView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        logoutMenuItem = menu.findItem(R.id.action_logout)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Perform logout action
                loginViewModel.logout()
                // Navigate to the login fragment
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val userEmail = sharedPreferences.getString(MainApplication.USER_EMAIL_KEY, null)
        logoutMenuItem?.isVisible = !userEmail.isNullOrEmpty()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}