package postgresql.model

data class User(
        var id: Int = 0,

        var login: String = "",

        var password: String = "",
) {



    //TODO: переделать
    fun getAllFields(): ArrayList<String> {
        val fields = ArrayList<String>()
//        fields.add(this.id.toString())
        fields.add("'" + this.login + "'")
        fields.add("'" + this.password + "'")
        return fields
    }

}