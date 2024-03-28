package database.interfaces
import model.User
public interface UserCollection{
    fun findAll(): List<User>

    fun findById(id: Long): User
    fun findByLogin(login: String): User

    fun save(user: User)
    fun checkUser(user: User): Boolean
}

public fun UserCollection.checkLogin(login: String): Boolean{
    return try{
        findByLogin(login)
        true
    } catch (e: Exception){
        false
    }
}