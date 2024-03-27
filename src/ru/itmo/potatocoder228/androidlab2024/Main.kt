import database.UserDB
import model.User


fun main() {
    val user = User("ee Doe", "passw0rd")

    val userDB = UserDB()
    val s = userDB.findAll()

    for (a in s) {
        println(a.login + "\t" + a.password + "\t" + a.id)
    }
}