package sk.upjs.hackstock

import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import sk.upjs.hackstock.dao.ShareDao
import sk.upjs.hackstock.dao.UserDao
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
import java.util.Date
import java.util.UUID


@Database(entities = [User::class, Share::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun usersDao(): UserDao
    abstract fun sharesDao(): ShareDao

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
                                    it.usersDao().insertUser(User("Jozko", "Mrkvicka", Date(),"USA", "NYC", "married", "doctor", 200000.00, 100.00, 100 ))
                                    var jozko= it.usersDao().getAllUsersWithNameAndSurname("Jozko", "Mrkvicka")
                                    if(jozko.toList().size==1){
                                        it.sharesDao().insertShare(Share(jozko.toList().get(0).get(0).userId,"Google", "GOOG", 10.00, 1.0))
                                    }
                                    it.usersDao().insertUser(User("Petko", "Mrkvicka", Date(),"USA", "NYC", "married", "doctor", 200000.00, 100.00, 100 ))
                                    var petko= it.usersDao().getAllUsersWithNameAndSurname("Petko", "Mrkvicka")
                                    if(petko.toList().size==1){
                                        it.sharesDao().insertShare(Share(jozko.toList().get(0).get(0).userId,"Apple", "APPL", 40.00, 1.0))
                                    }

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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Perform the migration operations
        database.execSQL("ALTER TABLE user ADD COLUMN dateOfBirth LONG")
    }
}

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