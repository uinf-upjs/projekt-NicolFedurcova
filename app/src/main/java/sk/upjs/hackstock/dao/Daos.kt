package sk.upjs.hackstock.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import sk.upjs.hackstock.entities.User
import kotlinx.coroutines.flow.Flow
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import java.util.Date

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE (name=:name AND surname=:surname)")
    fun getAllUsersWithNameAndSurname(name: String, surname: String): Flow<List<User>>

    @Query("SELECT * FROM user WHERE (email=:username AND password =:password)")
    suspend fun getUserByEmailAndPassword(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE (email=:username)")
    suspend fun getUserByEmail(username: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("""
        UPDATE user 
        SET name = :name, 
            surname = :surname, 
            dateOfBirth = :dateOfBirth, 
            country = :country, 
            city = :city, 
            status = :status, 
            occupation = :occupation, 
            annualIncome = :annualIncome
        WHERE userId = :userId
    """)
    suspend fun updateUserDetails(
        userId: Long,
        name: String,
        surname: String,
        dateOfBirth: Date,
        country: String,
        city: String,
        status: String,
        occupation: String,
        annualIncome: Double
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE user SET money = :money WHERE userId = :userId")
    suspend fun updateUserMoney(userId: Long, money: Double)

    @Query("DELETE FROM user WHERE (name=:name AND surname=:surname)")
    suspend fun deleteUserByNameSurnameBirthdate(name: String, surname: String)

    @Query("SELECT money FROM user WHERE userId = :userId")
    fun getUserMoneyLiveData(userId: Long): LiveData<Double>

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
    suspend fun insertShare(share: Share): Long

    @Query("SELECT * FROM share WHERE (shareId=:shareId)")
    suspend fun getShareById(shareId: Long): Share?

    @Query("SELECT * FROM share WHERE (userId= :userId AND company=:company AND shortname=:shortName)")
    suspend fun getShareByNameAndSymbolAndUser(company: String, shortName:String, userId: Long): Share?

    @Delete
    suspend fun deleteShare(share: Share)

    @Query("DELETE FROM share WHERE (company=:company)")
    suspend fun deleteShareByCompany(company: String)

    @Query("DELETE FROM share")
    suspend fun deleteAllShares()

    @Query("SELECT * FROM share WHERE userId = :userId AND visibility = 1")
    fun getVisibleSharesOfUser(userId: Long): Flow<List<Share>>

    @Query("SELECT * FROM share WHERE (userId = :userId AND company= :company AND shortname= :shortName AND price= :price AND amount= :amount AND visibility = :visibility)")
    fun getShareByDetails(userId: Long, company: String, shortName: String, price: Double, amount: Double, visibility: Byte): Flow<List<Share>>

    @Query("UPDATE share SET visibility = 0 WHERE (shareId= :shareId )") //0 je ekvivalent NULL
    suspend fun setInvisible(shareId:Long)

    @Query("UPDATE share SET visibility = 1 WHERE (shareId= :shareId )") //0 je ekvivalent NULL
    suspend fun setVisible(shareId:Long)

    @Query("UPDATE share SET amount = :amount WHERE (shareId= :shareId )") //0 je ekvivalent NULL
    suspend fun updateShareAmount(shareId:Long, amount: Double)

    @Query("UPDATE share SET price = :price WHERE (shareId= :shareId )") //0 je ekvivalent NULL
    suspend fun updateSharePrice(shareId:Long, price: Double)
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