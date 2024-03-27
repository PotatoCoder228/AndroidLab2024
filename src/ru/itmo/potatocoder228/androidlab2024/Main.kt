import database.UserDB
import model.User
import repository.UserRepository


fun main() {
    val repo = UserRepository()
    repo.initDB()
    val userDB = UserDB(repo)
    val user = User("234q", "passw330rd", 10)
    val a = userDB.checkUser(user)
    println(a)
    repo.closeConnection()

}