package sk.upjs.hackstock.repositories

import kotlinx.coroutines.flow.Flow
import sk.upjs.hackstock.dao.ShareDao
import sk.upjs.hackstock.dao.UserDao
import sk.upjs.hackstock.entities.User

class AppRepository(
    private val userDao: UserDao,
    private val shareDao: ShareDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
}