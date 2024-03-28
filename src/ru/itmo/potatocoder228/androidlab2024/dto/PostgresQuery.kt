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
    SELECT_BY_ID,
    SELECT_BY_LOGIN,
    SELECT_ALL,
    INSERT,
    UPDATE,
    DELETE_BY_ID,
    DELETE_BY_LOGIN,
    CHECK
}

