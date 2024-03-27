package postgresql.model

data class Owner(
        private var id: Int = 0,

        private var login: String = "",

        private var password: String = "",
) {

    fun getId(): Int {
        return this.id
    }

    fun getLogin(): String {
        return this.login
    }

    fun getPassword(): String {
        return this.password
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun setLogin(login: String) {
        this.login = login
    }

    fun setPassword(password: String) {
        this.password = password
    }


    //TODO: переделать
    fun getAllFields(): ArrayList<String> {
        val fields = ArrayList<String>()
        fields.add(this.getId().toString())
        fields.add("'" + this.getLogin() + "'")
        fields.add("'" + this.getPassword() + "'")
        return fields
    }

}