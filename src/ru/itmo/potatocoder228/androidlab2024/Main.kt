import postgresql.model.*
import postgresql.repo.OwnerRepo


fun main() {
    val owner = Owner(123, "ee Doe", "passw0rd")

    val repo = OwnerRepo()
    val s = repo.getOwners()

    for (a in s) {
        println(a.getId().toString() + "\t" + a.getLogin() + "\t" + a.getPassword())
    }
}