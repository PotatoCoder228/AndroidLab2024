
package database
import kotlin.collections.emptyList
import model.User
import database.interfaces.*
public class UserDB : UserCollection{
    var collection = mutableListOf<User>()
    override fun addUser(user: User){
        collection.add(user)
    }

}