package dto

import model.User

data class PostgresQuery(
        private var type: PostgresQueryType,
        private var user: User,
){
    constructor(type: PostgresQueryType) : this(type, User("","", 0))

    fun setType(type: PostgresQueryType) {
        this.type = type
    }

    fun setUser(user: User) {
        this.user = user
    }


    fun getType() : PostgresQueryType {
        return this.type
    }

    fun getUser(): User {
        return this.user
    }


}


enum class PostgresQueryType {
    SELECT_ONE,
    SELECT_ALL,
    INSERT,
    UPDATE,
    DELETE,
    CHECK
}


data class PostgresQueryResult(
        private var message: String,
        private var collection: ArrayList<User>
) {
    constructor(message: String) : this(message, ArrayList())
    constructor() : this("", ArrayList())



    fun setMessage(message: String) {
        this.message = message
    }

    fun setCollection(collection: ArrayList<User>) {
        this.collection = collection
    }


    fun getMessage() : String {
        return this.message
    }

    fun getCollection(): ArrayList<User>{
        return this.collection
    }

}