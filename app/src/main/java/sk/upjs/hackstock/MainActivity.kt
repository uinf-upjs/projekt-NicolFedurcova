package sk.upjs.hackstock

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import sk.upjs.hackstock.databinding.ActivityMainBinding
import sk.upjs.hackstock.ui.home.HomeViewModel
import sk.upjs.hackstock.ui.login.LoginViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels{
        LoginViewModel.LoginViewModelFactory((application as MainApplication).repository)
    }
    private var logoutMenuItem: MenuItem? = null
    private var profileMenuItem: MenuItem? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var moneyTextView: TextView
    private lateinit var homeViewModel: HomeViewModel

    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == MainApplication.USER_ID_KEY) {
            val newUserId = sharedPreferences.getLong(key, 0)
            homeViewModel.setUserIds(newUserId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = (application as MainApplication).getSharedPreferences()
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefsListener)

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
            if (destination.id == R.id.profile_settings_fragment) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
            invalidateOptionsMenu()
            checkUserStatus()

        }



        checkUserStatus()

        val factory = HomeViewModel.HomeViewModelFactory((application as MainApplication).repository, this.applicationContext)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        homeViewModel.userMoney.observe(this) { money ->
            if(money!=null){
                Log.e("TUU", money.toString())
                // Update the top bar UI with the user's money attribute if the menu is created
                if (::moneyTextView.isInitialized) {
                    val num = formatNumberToThreeDecimalPlaces(money)
                    moneyTextView.text = "$$num"
                }
            }
        }

    }

    fun formatNumberToThreeDecimalPlaces(number: Double): String {
        return String.format("%.2f", number)
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
        profileMenuItem = menu.findItem(R.id.action_settings)


        val actionView = menu.findItem(R.id.action_money).actionView
        if (actionView != null) {
            moneyTextView = actionView.findViewById(R.id.textViewMoney)
        }

        try{
            homeViewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory((application as MainApplication).repository, this.applicationContext)).get(HomeViewModel::class.java)
            homeViewModel.userMoney.observe(this) { money ->
                if(money!=null){
                    Log.e("TUU", money.toString())
                    val num = formatNumberToThreeDecimalPlaces(money)
                    moneyTextView.text = "$$num"
                }
            }
        }catch (e: Exception) {
            Log.e("TUU", e.toString())
        }


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
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_to_profileSettingsFragment)
                binding.navView.visibility = View.INVISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val userEmail = sharedPreferences.getString(MainApplication.USER_EMAIL_KEY, null)
        profileMenuItem?.isVisible = !userEmail.isNullOrEmpty()
        logoutMenuItem?.isVisible = !userEmail.isNullOrEmpty()
        moneyTextView?.isVisible = !userEmail.isNullOrEmpty()

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}