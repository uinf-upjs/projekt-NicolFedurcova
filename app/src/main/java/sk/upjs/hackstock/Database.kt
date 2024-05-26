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
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
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
                                    it.usersDao().deleteAllUsers()
                                    it.sharesDao().deleteAllShares()
                                    it.activityDao().deleteAllActivities()
                                    it.questionDao().deleteAllQuestions()

                                    it.usersDao().insertUser(User("Jozko", "Mrkvicka", "jm","hslo",Date(),"USA", "NYC", "married", "doctor", 200000.00, 100.00, 100 ))
                                    it.sharesDao().insertShare(Share(1,"Google", "GOOG", -10.00, 1.0))
                                    it.sharesDao().insertShare(Share(1,"Amazon", "AMZN", -20.00, 1.0))
                                    it.sharesDao().insertShare(Share(1,"Facebook", "META", -50.00, 1.0))

                                    it.usersDao().insertUser(User("Petko", "Mrkvicka", "petko.mrkvicka@gmail.com","hslo", Date(),"USA", "NYC", "married", "doctor", 200000.00, 100.00, 100 ))
                                    it.sharesDao().insertShare(Share(2,"Apple", "AAPL", -40.00, 1.0))
                                    it.activityDao().insertActivity(Activity(1,1,Date(),false,false))
                                    it.activityDao().insertActivity(Activity(1,3,Date(),false,false))
                                    it.activityDao().insertActivity(Activity(1,4,Date(),false,false))
                                    it.activityDao().insertActivity(Activity(2,2,Date(),false,false))
                                    it.questionDao().insertQuestion(Question("What is share", "A)", "B)", "C) correct"))
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