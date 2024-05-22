package sk.upjs.hackstock.repositories

import sk.upjs.hackstock.dao.UsersDao

class UsersRepository(private val usersDao: UsersDao) {
    val users = usersDao.getAllUsers()
}