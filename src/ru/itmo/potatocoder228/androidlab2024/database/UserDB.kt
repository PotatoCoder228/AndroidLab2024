package database

import model.User
import database.interfaces.*
import exceptions.DBException
import repository.UserRepository
import dto.PostgresQuery
import dto.PostgresQueryType.*


class UserDB() : UserCollection {
    private val repo = UserRepository()
    fun initUserDB(): Boolean {
        return repo.initRepo()
    }

    private val collection = ArrayList<User>()

    override fun findAll(): List<User> {
        repo.queryExecutor(PostgresQuery(SELECT_ALL), collection)
        return collection
    }

    override fun findById(id: Long): User {
        val user = User(id)
        return try {
            repo.queryExecutor(PostgresQuery(SELECT_BY_ID, user), collection)
            if (collection.isEmpty()) throw DBException()
            else collection[0]
        } catch (_: DBException) {
            User("", "", 0)
        }
    }

    override fun findByLogin(login: String): User {
        val user = User(login)
        return try {
            repo.queryExecutor(PostgresQuery(SELECT_BY_LOGIN, user), collection)
            if (collection.isEmpty()) throw DBException()
            else collection[0]
        } catch (_: DBException) {
            User.nullUser
        }
    }

    override fun save(user: User) {
        try {
            val res = repo.queryExecutor(PostgresQuery(INSERT, user))
            if (!res) throw DBException()
        } catch (_: DBException) {
        }
    }


    override fun checkUser(user: User): Boolean {
        return repo.queryExecutor(PostgresQuery(CHECK, user))
    }


    override fun deleteById(id: Long) {
        val user = User(id)
        try {
            val res = repo.queryExecutor(PostgresQuery(DELETE_BY_ID, user))
            if (!res) throw DBException()
        } catch (_: DBException) {
        }
    }

    override fun deleteByLogin(login: String) {
        val user = User(login)
        try {
            val res = repo.queryExecutor(PostgresQuery(DELETE_BY_LOGIN, user))
            if (!res) throw DBException()
        } catch (_: DBException) {
        }
    }


    override fun update(user: User) {
        try {
            val res = repo.queryExecutor(PostgresQuery(UPDATE, user))
            if (!res) throw DBException()
        } catch (_: DBException) {
        }
    }


}