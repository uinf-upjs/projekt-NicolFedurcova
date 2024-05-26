package sk.upjs.hackstock.repositories

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

class AppRepository(
    private val userDao: UserDao,
    private val shareDao: ShareDao,
    private val activityDao: ActivityDao,
    private val questionDao: QuestionDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()
    val allActivity: Flow<List<Activity>> = activityDao.getAllActivities()
    fun sharesOfUser(userId:Long): Flow<List<Share>> = shareDao.getAllSharesOfUser(userId)
    fun activitiesOfUser(userId: Long): Flow<List<Activity>> = activityDao.getAllActivityOfUser(userId)

    suspend fun getUserByEmailAndPassword(email:String, password:String): User?{
        return userDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
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

}