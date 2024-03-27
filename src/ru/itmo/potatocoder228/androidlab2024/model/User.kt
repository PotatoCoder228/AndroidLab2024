package model
public data class User(
        var login: String,
        var password: String,
        var id: Int = 0,
){
    //TODO: переделать
    fun getAllFields(): ArrayList<String> {
        val fields = ArrayList<String>()
        fields.add("'" + this.login + "'")
        fields.add("'" + this.password + "'")
        fields.add(this.id.toString())
        return fields
    }
}