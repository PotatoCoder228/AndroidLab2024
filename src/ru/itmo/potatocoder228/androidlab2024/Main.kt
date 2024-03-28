import database.UserDB
import model.User
import repository.UserRepository


fun main() {
    val userDB = UserDB()
    userDB.initUserDB()
    val user = User("11", "11")
    val a = userDB.findByLogin("22")
    println(a)
//    for(i in a){
//        println(i)
//    }

//    println(a)

//    val s = repo.getStringFieldsPairsStructure()
//    println(s)

}



//CREATE TABLE IF NOT EXISTS OWNER (id Int32 primary key, login String , password String );
