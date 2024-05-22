package sk.upjs.hackstock

import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import sk.upjs.hackstock.dao.UsersDao
import sk.upjs.hackstock.entities.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}