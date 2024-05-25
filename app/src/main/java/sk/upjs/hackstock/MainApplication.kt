package sk.upjs.hackstock

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import sk.upjs.hackstock.repositories.AppRepository

class MainApplication : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
        const val PREFS_NAME = "user_preferences"
        const val USER_EMAIL_KEY = "user_email"
    }

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(database.usersDao(), database.sharesDao(), database.activityDao(), database.questionDao()) }

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }



    fun getSharedPreferences(): SharedPreferences {
        return prefs
    }



}