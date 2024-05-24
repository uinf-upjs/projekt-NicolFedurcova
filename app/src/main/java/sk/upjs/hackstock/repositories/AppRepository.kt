package sk.upjs.hackstock.repositories

import kotlinx.coroutines.flow.Flow
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

}