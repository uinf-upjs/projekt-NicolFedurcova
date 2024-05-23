package sk.upjs.hackstock.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.upjs.hackstock.entities.User
import java.util.Date
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user WHERE (name=:name AND surname=:surname)")
    suspend fun deleteUserByNameSurnameBirthdate(name: String, surname: String)

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}