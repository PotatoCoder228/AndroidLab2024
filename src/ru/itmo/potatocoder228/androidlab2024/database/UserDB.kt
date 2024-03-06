

import kotlin.collections.emptyList
public class UserDB : UserCollection{
    var collection = mutableListOf<User>()
    override fun addUser(user: User){
        collection.add(user)
    }

}