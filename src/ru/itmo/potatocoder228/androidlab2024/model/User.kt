package model
public data class User(
        var login: String,
        var password: String,
        var id: Long = 0,
) {
    companion object{
        val nullUser = User("", "", 0)
    }
    constructor(id: Long) : this("", "", id)
    constructor(login: String) : this(login, "", 0)

}