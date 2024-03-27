package postgresql.model

data class Query(
        private var type: QueryType,
        private var owner: Owner,
){
    constructor(type: QueryType) : this(type, Owner(0,"",""))

    fun setType(type: QueryType) {
        this.type = type
    }

    fun setOwner(owner: Owner) {
        this.owner = owner
    }


    fun getType() : QueryType {
        return this.type
    }

    fun getOwner(): Owner{
        return this.owner
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
        private var collection: ArrayList<Owner>
) {
    constructor(message: String) : this(message, ArrayList())
    constructor() : this("", ArrayList())



    fun setMessage(message: String) {
        this.message = message
    }

    fun setCollection(collection: ArrayList<Owner>) {
        this.collection = collection
    }


    fun getMessage() : String {
        return this.message
    }

    fun getCollection(): ArrayList<Owner>{
        return this.collection
    }

}