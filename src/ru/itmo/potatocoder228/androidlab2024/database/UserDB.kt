
package database
import kotlin.collections.emptyList
import model.User
import database.interfaces.*
public class UserDB : UserCollection{
    var collection = mutableListOf<User>()

    override fun findAll(): List<User> = collection

    override fun findById(id: Int): User? =
        collection.firstOrNull { it.id == id }

    override fun findByLogin(login: String): User? =
        collection.firstOrNull { it.login == login }

    override fun save(user: User): Boolean =
        collection.add(user)

    override fun checkUser(user: User): Boolean{
        val dbUser = findByLogin(user.login)
        return dbUser?.login==user.login && dbUser?.password == user.password
    }

}