package database.interfaces
import model.User
public interface UserCollection{
    fun findAll(): List<User>

    fun findById(id: Int): User?
    fun findByLogin(login: String): User?

    fun save(user: User): Boolean
    fun checkUser(user: User): Boolean
}