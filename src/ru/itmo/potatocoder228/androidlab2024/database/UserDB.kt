package database

import model.User
import database.interfaces.*
import exceptions.DBException
import repository.UserRepository
import dto.PostgresQuery
import dto.PostgresQueryType.*


class UserDB(private val repo : UserRepository) : UserCollection {

    override fun findAll(): List<User> {
        val collection = repo.queryExecutor(PostgresQuery(SELECT_ALL)).getCollection()
        return collection
    }

    override fun findById(id: Int): User {
        TODO("Not yet implemented")
    }

    override fun findByLogin(login: String): User {
        TODO("Not yet implemented")
    }


    override fun save(user: User) {
        repo.queryExecutor(PostgresQuery(INSERT, user))
    }


    override fun checkUser(user: User): Boolean {
        val res = repo.queryExecutor(PostgresQuery(CHECK, user)).getMessage()
        return res == "t"
    }


    //TODO: не работает
    override fun deleteById(id: Int) {
        //manager.initConnection()
        //manager.queryExecutor(PostgresQuery(DELETE, user)).getMessage()
        //manager.closeConnection()
    }


    override fun update(user: User) {
        repo.queryExecutor(PostgresQuery(UPDATE, user)).getMessage()
    }


}