package sk.upjs.hackstock.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.upjs.hackstock.entities.User
import kotlinx.coroutines.flow.Flow
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE (name=:name AND surname=:surname)")
    fun getAllUsersWithNameAndSurname(name: String, surname: String): Flow<List<User>>

    @Query("SELECT * FROM user WHERE (email=:username AND password =:password)")
    suspend fun getUserByEmailAndPassword(username: String, password: String): User?

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

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activity")
    fun getAllActivities(): Flow<List<Activity>>

    @Query("SELECT * FROM activity WHERE userId=:userId")
    fun getAllActivityOfUser(userId:Long): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)

    @Query("DELETE FROM activity WHERE (userId=:userId)")
    suspend fun deleteActivityByUserId(userId: Long)

    @Query("DELETE FROM activity")
    suspend fun deleteAllActivities()
}

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question")
    fun getAllQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM question WHERE (text=:text)")
    suspend fun deleteQuestionByText(text: String)

    @Query("DELETE FROM question")
    suspend fun deleteAllQuestions()
}