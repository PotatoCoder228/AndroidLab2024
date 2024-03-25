
package database
import kotlin.collections.emptyList
import model.User
import database.interfaces.*
import exceptions.DBException
public class UserDB : UserCollection{
    var collection = mutableListOf<User>()
    companion object { 
        var lastId : Int = 0
    }
    override fun findAll(): List<User> = collection

    override fun findById(id: Int): User =
        collection.firstOrNull { it.id == id }?: throw DBException()

    override fun findByLogin(login: String): User =
        collection.firstOrNull { it.login == login } ?: throw DBException()

    override fun save(user: User) {
        user.id = lastId;
        lastId++;
        collection.add(user) || throw DBException();
    }

    override fun checkUser(user: User): Boolean{
        return try{
            val dbUser = findByLogin(user.login)
            dbUser.login==user.login && dbUser.password == user.password
        } catch (e: DBException){
            false
        }
    }

}