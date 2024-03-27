
package database
import kotlin.collections.emptyList
import model.User
import database.interfaces.*
import exceptions.DBException
import database.DBManager.DBManager
import dto.PostgresQuery
import dto.PostgresQueryType.*
import dto.PostgresQueryResult
import postgresql.model.Query
import postgresql.model.QueryType

public class UserDB : UserCollection{
    private var manager: DBManager = DBManager()
    var collection = mutableListOf<User>()
    companion object {
        var lastId : Int = 0
    }
    override fun findAll(): List<User>{
        manager.initConnection()
        collection = manager.queryExecutor(PostgresQuery(SELECT_ALL)).getCollection()
        return collection
    }

    //TODO: не работает
    override fun findById(id: Int): User =
            collection.firstOrNull { it.id == id }?: throw DBException()

    //TODO: не работает
    override fun findByLogin(login: String): User =
            collection.firstOrNull { it.login == login } ?: throw DBException()

    override fun save(user: User) {
        manager.initConnection()
        user.id = lastId;
        lastId++;
        manager.queryExecutor(PostgresQuery(DELETE, user))
        manager.closeConnection()
    }


    //TODO: не работает
    override fun checkUser(user: User): Boolean{
        return try{
            val dbUser = findByLogin(user.login)
            dbUser.login==user.login && dbUser.password == user.password
        } catch (e: DBException){
            false
        }
    }


    //TODO: не работает
    override fun deleteById(id: Int) {
        //manager.initConnection()
        //manager.queryExecutor(PostgresQuery(DELETE, user)).getMessage()
        //manager.closeConnection()
    }


    override fun update(user: User) {
        manager.initConnection()
        manager.queryExecutor(PostgresQuery(UPDATE, user)).getMessage()
        manager.closeConnection()
    }


}