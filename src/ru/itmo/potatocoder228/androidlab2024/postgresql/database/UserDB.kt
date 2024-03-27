package postgresql.database

import postgresql.database.dbManager.*
import postgresql.model.User
import postgresql.model.Query
import postgresql.model.QueryType.*

class UserDB {
    private var manager: DBManager = DBManager()


    fun findAll(): List<User> {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(SELECT_ALL)).getCollection()
        manager.closeConnection()
        return toRet
    }



//    fun findById(id: Int): User {
//        manager.initConnection()
//        val toRet = manager.queryExecutor(Query(SELECT_ALL)).getCollection()
//        manager.closeConnection()
//        return toRet
//    }

//    fun findByLogin(login: String): User {
//        manager.initConnection()
//        val toRet = manager.queryExecutor(Query(SELECT_ALL)).getCollection()
//        manager.closeConnection()
//        return toRet
//    }

    fun save(user: User): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(INSERT, user)).getMessage()
        manager.closeConnection()
        return toRet
    }

    fun deleteOwner(user: User): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(DELETE, user)).getMessage()
        manager.closeConnection()
        return toRet
    }

    fun updateOwner(user: User): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(UPDATE, user)).getMessage()
        manager.closeConnection()
        return toRet
    }

}