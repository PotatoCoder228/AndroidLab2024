package postgresql.database.dbManager

import postgresql.model.User
import postgresql.model.Query
import postgresql.model.QueryResult
import postgresql.model.QueryType.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

class DBManager {
    private lateinit var connection: Connection

    fun initConnection(): Boolean {

        val url = "jdbc:postgresql://localhost/postgres"
        val properties = Properties()

        properties["user"] = "postgres"
        properties["password"] = "keytoalldoors"

        try {
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(url, properties)
            println("initConnection: OK")
            return true
        } catch (e: Exception) {
            println("initConnection: ${e.message}")
            return false
        }
    }

    fun queryExecutor(query: Query): QueryResult {
        if (connection.isClosed) initConnection()
        val statement = connection.createStatement()
        val insertQuery = buildQuery(query)

        val queryResult = QueryResult()
        try {

            if (query.getType() == SELECT_ALL) {
                statement.execute(insertQuery)
                val resultSet: ResultSet = statement.resultSet

                val collection = ArrayList<User>()
                while (resultSet.next())
                    //TODO: как отойти от привязки к полям класса
                    collection.add(User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password")))
                queryResult.setCollection(collection)
                resultSet.close()
                statement.close()
            } else
                statement.execute(insertQuery)

            queryResult.setMessage("queryExecutor: OK")
            return queryResult
        } catch (e: Exception) {
            queryResult.setMessage("queryExecutor: ${e.message}")
            return queryResult
        }
    }



//TODO: в Query добавить parameter?
    private fun buildQuery(query: Query): String {
        val queryString: String = when (query.getType()) {
            SELECT_ONE -> selectOneQuery() //не нужен owner
            SELECT_ALL -> selectAllQuery() //получает query без owner
            INSERT -> insertQuery(query)
            UPDATE -> updateQuery(query)
            DELETE -> deleteQuery(query) //не нужен owner
        }
        return queryString
    }


    private fun selectAllQuery(): String {
        return "SELECT * FROM OWNER;".trimIndent()
    }

    private fun selectOneQuery(): String {
        val parameter = "1"
        val s = "id"
        //s = "login"
        //parameter = "'1'"
        //s = "password"

        return "SELECT * FROM OWNER WHERE $s=$parameter;".trimIndent()
    }


    private fun insertQuery(query: Query): String {
        val fields = query.getOwner().getAllFields()
        var s = ""
        for (i in 0..<fields.size) {
            s += fields[i]
            if (i != fields.size - 1)
                s += ", "
        }
        return "INSERT INTO OWNER VALUES(${s});".trimIndent()
    }


    private fun updateQuery(query: Query): String {
        val fields = query.getOwner().getAllFields()
        val classFields = Class.forName("postgresql.Model.Owner").declaredFields

        var s = ""
        for (i in 0..<fields.size) {
            s += (classFields[i].name + "=" + fields[i])
            if (i != fields.size - 1)
                s += ", "
        }
        return "UPDATE OWNER SET $s WHERE id=${query.getOwner().id};"
    }


    private fun deleteQuery(query: Query): String {
        return "DELETE FROM OWNER WHERE id=${query.getOwner().id};".trimIndent()
    }

    fun closeConnection() {
        connection.close()
        println("closeConnection: OK")
    }

}


//    fun createExampleTable() : Boolean {
//        val createTableQuery = """
//        CREATE TABLE if not exists OWNER (
//          id SERIAL PRIMARY KEY not null,
//          login VARCHAR(32) UNIQUE not null,
//          password text not null
//        );
//
//    """.trimIndent()
//
//        val statement = connection.createStatement()
//        try{
//            statement.execute(createTableQuery)
//            println("Таблица MY_LOG успешно создана в базе данных!")
//            return true
//        } catch (e : Exception){
//            println("Таблица MY_LOG не создана в базе данных!")
//            return false
//        }
//    }