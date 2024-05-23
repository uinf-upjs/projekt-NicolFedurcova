package sk.upjs.hackstock

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import sk.upjs.hackstock.repositories.AppRepository

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(database.usersDao(), database.sharesDao()) }

}