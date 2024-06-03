package sk.upjs.hackstock.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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
import sk.upjs.hackstock.ui.search.SearchResult
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

    fun getUserMoneyLiveData(userId: Long): LiveData<Double> {
        return userDao.getUserMoneyLiveData(userId)
    }

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

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

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
    ) {
        userDao.updateUserDetails(
            userId, name, surname, dateOfBirth, country, city, status, occupation, annualIncome
        )
    }

    suspend fun deleteShare(share: Share){
        shareDao.deleteShare(share)
    }

    suspend fun getUsersMoney(userName: String): Double {
        return userDao.getUserByEmail(userName)?.money ?: 0.0
    }

    suspend fun getUserByEmail(userName: String): User? {
        return userDao.getUserByEmail(userName)
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
    suspend fun sellShare(userName: String, priceOfShare: Double, share: Share, amount2: Double) { //TREBA DODAT AMOUNT!!!
        val userMoney = userDao.getUserByEmail(userName)?.money ?: 0.0
        val user = userDao.getUserByEmail(userName)
        if (user != null) {
            userDao.updateUserMoney(user.userId, userMoney+(priceOfShare*amount2))
        }
        Log.e("TU", "share ID: ${share.shareId}")

        if(share.amount > amount2){
            shareDao.updateShareAmount(share.shareId, share.amount-amount2)
            shareDao.updateSharePrice(share.shareId, share.price+(priceOfShare*amount2))
        } else {
            //nastane len ak sa rovnaju
            shareDao.updateShareAmount(share.shareId, share.amount-amount2)
            shareDao.updateSharePrice(share.shareId, share.price+(priceOfShare*amount2))
            shareDao.setInvisible(share.shareId)
        }

        if (user != null) {
            activityDao.insertActivity(Activity(user.userId, share.shareId,
                Date(), true, (priceOfShare*amount2)))
        }
        Log.e("TU", "Successfully sold share")
    }

    @Transaction
    suspend fun buyShare(userName: String, priceOfShare: Double, amount2:Double, searchResult: SearchResult):Boolean {
        val userMoney = userDao.getUserByEmail(userName)?.money ?: 0.0
        val user = userDao.getUserByEmail(userName)
        lateinit var share: Share

        if (user != null && userMoney >= (priceOfShare*amount2)) {
            userDao.updateUserMoney(user.userId, userMoney-(priceOfShare*amount2))
        } else if(userMoney<priceOfShare){
            return false
        }

        if (user != null) {
            var shareId: Long
            val existingShare=shareDao.getShareByNameAndSymbolAndUser(searchResult.name, searchResult.symbol, user.userId)
            if(existingShare!=null){
                shareId = existingShare.shareId
                shareDao.updateShareAmount(shareId, existingShare.amount+amount2)
                shareDao.updateSharePrice(shareId, existingShare.price-(priceOfShare*amount2))
                shareDao.setVisible(shareId)
            }else{
                shareId = shareDao.insertShare(Share(user.userId, searchResult.name, searchResult.symbol, -(priceOfShare*amount2), amount = amount2, visibility = 1))
            }
            share = shareDao.getShareById(shareId)!!
        }
        Log.e("TU", "new share created")
        if (user != null && share != null){
            activityDao.insertActivity(Activity(user.userId, share.shareId,
                Date(), false, -(priceOfShare*amount2)))
        }
        Log.e("TU", "Successfully bought share and activity created")
        return true
    }



}