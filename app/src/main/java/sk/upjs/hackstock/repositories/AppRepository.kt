package sk.upjs.hackstock.repositories

import android.util.Log
import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.dao.ActivityDao
import sk.upjs.hackstock.dao.QuestionDao
import sk.upjs.hackstock.dao.ShareDao
import sk.upjs.hackstock.dao.UserDao
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
import java.util.Date

class AppRepository(
    private val userDao: UserDao,
    private val shareDao: ShareDao,
    private val activityDao: ActivityDao,
    private val questionDao: QuestionDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()
    val allActivity: Flow<List<Activity>> = activityDao.getAllActivities()
    fun sharesOfUser(userId:Long): Flow<List<Share>> = shareDao.getAllSharesOfUser(userId)

    fun visibleSharesOfUser(userId:Long): Flow<List<Share>> = shareDao.getVisibleSharesOfUser(userId)
    fun activitiesOfUser(userId: Long): Flow<List<Activity>> = activityDao.getAllActivityOfUser(userId)

    suspend fun getUserByEmailAndPassword(email:String, password:String): User?{
        return userDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun insertUsers(users: List<User>) {
        users.forEach { userDao.insertUser(it) }
    }

    suspend fun insertShares(shares: List<Share>) {
        shares.forEach { shareDao.insertShare(it) }
    }

    suspend fun insertActivities(activities: List<Activity>) {
        activities.forEach { activityDao.insertActivity(it) }
    }

    suspend fun insertQuestions(questions: List<Question>) {
        questions.forEach { questionDao.insertQuestion(it) }
    }

    suspend fun deleteShare(share: Share){
        shareDao.deleteShare(share)
    }

    suspend fun getUsersMoney(userName: String): Double {
        return userDao.getUserByEmail(userName)?.money ?: 0.0
    }

    suspend fun updateUsersMoney(userName: String, amount:Double) {
        val oldUser = userDao.getUserByEmail(userName)
        if (oldUser != null) {
            return userDao.updateUserMoney(oldUser.userId, amount)
        }
    }

    suspend fun clearUserSession() {
        withContext(Dispatchers.IO) {
            // Clear user data from SharedPreferences
            MainApplication.prefs.edit().clear().apply()
        }
    }

    @Transaction
    suspend fun sellShare(userName: String, priceOfShare: Double, share: Share) {
        val userMoney = userDao.getUserByEmail(userName)?.money ?: 0.0
        val user = userDao.getUserByEmail(userName)
        if (user != null) {
            userDao.updateUserMoney(user.userId, userMoney+priceOfShare)
        }
        Log.e("SETNULLSHAREUSER", "share ID: ${share.shareId}")

        shareDao.setInvisible(share.shareId)
        //shareDao.deleteShare(share)
        if (user != null) {
            activityDao.insertActivity(Activity(user.userId, share.shareId,
                Date(), true, priceOfShare))
        }
        Log.e("SUCCESSELL", "Successfully sold share")
    }



}