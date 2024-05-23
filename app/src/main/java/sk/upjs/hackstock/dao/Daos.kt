package sk.upjs.hackstock.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.upjs.hackstock.entities.User
import kotlinx.coroutines.flow.Flow
import sk.upjs.hackstock.entities.Share

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE (name=:name AND surname=:surname)")
    fun getAllUsersWithNameAndSurname(name: String, surname: String): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user WHERE (name=:name AND surname=:surname)")
    suspend fun deleteUserByNameSurnameBirthdate(name: String, surname: String)

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}

@Dao
interface ShareDao {

    @Query("SELECT * FROM share")
    fun getAllShares(): Flow<List<Share>>

    @Query("SELECT * FROM share WHERE userId=:userId")
    fun getAllSharesOfUser(userId:Long): Flow<List<Share>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShare(share: Share)

    @Delete
    suspend fun deleteShare(share: Share)

    @Query("DELETE FROM share WHERE (company=:company)")
    suspend fun deleteShareByCompany(company: String)

    @Query("DELETE FROM share")
    suspend fun deleteAllShares()
}