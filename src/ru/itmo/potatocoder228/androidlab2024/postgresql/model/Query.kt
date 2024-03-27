package postgresql.model

data class Query(
        private var type: QueryType,
        private var user: User,
){
    constructor(type: QueryType) : this(type, User(0,"",""))

    fun setType(type: QueryType) {
        this.type = type
    }

    fun setOwner(user: User) {
        this.user = user
    }


    fun getType() : QueryType {
        return this.type
    }

    fun getOwner(): User{
        return this.user
    }


}


enum class QueryType {
    SELECT_ONE,
    SELECT_ALL,
    INSERT,
    UPDATE,
    DELETE
}


data class QueryResult(
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