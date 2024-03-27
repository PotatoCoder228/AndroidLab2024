package database.DBManager

import model.User
import dto.PostgresQuery
import dto.PostgresQueryType
import dto.PostgresQueryResult
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

    fun queryExecutor(query: PostgresQuery): PostgresQueryResult {
        if (connection.isClosed) initConnection()
        val statement = connection.createStatement()
        val insertQuery = buildQuery(query)

        val queryResult = PostgresQueryResult()
        try {

            if (query.getType() == PostgresQueryType.SELECT_ALL) {
                statement.execute(insertQuery)
                val resultSet: ResultSet = statement.resultSet

                val collection = ArrayList<User>()
                while (resultSet.next())
                //TODO: как отойти от привязки к полям класса
                    collection.add(User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getInt("id")))
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



    //TODO: в PostgresQuery добавить parameter?
    private fun buildQuery(query: PostgresQuery): String {
        val queryString: String = when (query.getType()) {
            PostgresQueryType.SELECT_ONE -> selectOneQuery() //не нужен owner
            PostgresQueryType.SELECT_ALL -> selectAllQuery() //получает query без owner
            PostgresQueryType.INSERT -> insertQuery(query)
            PostgresQueryType.UPDATE -> updateQuery(query)
            PostgresQueryType.DELETE -> deleteQuery(query) //не нужен owner
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


    private fun insertQuery(query: PostgresQuery): String {
        val fields = query.getUser().getAllFields()
        var s = ""
        for (i in 0..<fields.size) {
            s += fields[i]
            if (i != fields.size - 1)
                s += ", "
        }
        return "INSERT INTO OWNER VALUES(${s});".trimIndent()
    }


    private fun updateQuery(query: PostgresQuery): String {
        val fields = query.getUser().getAllFields()
        val classFields = Class.forName("postgresql.Model.Owner").declaredFields

        var s = ""
        for (i in 0..<fields.size) {
            s += (classFields[i].name + "=" + fields[i])
            if (i != fields.size - 1)
                s += ", "
        }
        return "UPDATE OWNER SET $s WHERE id=${query.getUser().id};"
    }


    private fun deleteQuery(query: PostgresQuery): String {
        return "DELETE FROM OWNER WHERE id=${query.getUser().id};".trimIndent()
    }

    fun closeConnection() {
        connection.close()
        println("closeConnection: OK")
    }
}