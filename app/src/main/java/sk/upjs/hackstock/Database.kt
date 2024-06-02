package sk.upjs.hackstock

import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import sk.upjs.hackstock.dao.ActivityDao
import sk.upjs.hackstock.dao.QuestionDao
import sk.upjs.hackstock.dao.ShareDao
import sk.upjs.hackstock.dao.UserDao
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.InitialData
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository
import java.io.InputStreamReader
import java.util.Date
import java.util.UUID


@Database(entities = [User::class, Share::class, Activity::class, Question::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun usersDao(): UserDao
    abstract fun sharesDao(): ShareDao
    abstract fun activityDao(): ActivityDao
    abstract fun questionDao(): QuestionDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(
                    object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let {
                                scope.launch {
                                    prepopulateDatabase(it, context)
                                }
                            }
                        }
                    }
                )
//                    .addMigrations(
//                    MIGRATION_1_2
//                )
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(database: AppDatabase, context: Context) {
            val gson = Gson()
            val jsonFile = context.assets.open("initial_data.json")
            val reader = InputStreamReader(jsonFile)
            val initialDataType = object : TypeToken<InitialData>() {}.type
            val initialData: InitialData = gson.fromJson(reader, initialDataType)

            initialData.users.forEach{database.usersDao().insertUser(it)}
            initialData.shares.forEach{database.sharesDao().insertShare(it)}
            initialData.activities.forEach{database.activityDao().insertActivity(it)}
            initialData.questions.forEach{database.questionDao().insertQuestion(it)}
        }

    }


}

//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Perform the migration operations
//        database.execSQL("ALTER TABLE user ADD COLUMN dateOfBirth LONG")
//    }
//}


class Converters {
    @TypeConverter
    fun uuidToString(uuid: UUID) = uuid.toString()

    @TypeConverter
    fun stringToUuid(string: String) = UUID.fromString(string)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}