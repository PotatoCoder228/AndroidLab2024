package repository

import model.User
import dto.PostgresQuery
import dto.PostgresQueryType.*
import dto.PostgresQueryResult
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties


class UserRepository {
    private lateinit var connection: Connection
    private val tableName = "OWNER"
    lateinit var fields: Map<String, String>

    fun initDB(): Boolean {
        if (!initConnection()) return false;
        getUserFields();
//        createHouseTable();
        return true;
    }

    private fun translateToClickhouseDT(type: KType): String {
        return when (type.classifier) {
            Int::class -> "Int32"
            String::class -> "String"
            Boolean::class -> "Boolean"
            else -> throw Exception("Неопознанный тип данных")
        }
    }

    private fun getUserFields() {
        fields = User::class.memberProperties.associate { it.name to translateToClickhouseDT(it.returnType) }
    }

    /*
    Получить строковое представление всех названий полей и типов данных для класса House;
    Используется для CREATE TABLE:
 */
//    private fun getStringFieldsPairsStructure(): String {
//        return fields.entries.joinToString(", ") { entry ->
//            val key = entry.key
//            val value = entry.value
//            val primaryKey = if (key == "id") "primary key" else ""
//            "$key $value $primaryKey"
//        }
//    }

    /*
           Получить значения всех полей кроме id в строковом формате;
           Используется для INSERT INTO:
        */
    private fun getFieldValues(user: User): List<String> {
        val values = mutableListOf<String>()
        val fieldNames = fields.keys.toList()
        for (fieldName in fieldNames) {
            if (fieldName == "id") continue;
            var value = user::class.memberProperties.first { it.name == fieldName }.call(user)
            if (fields[fieldName].equals("String")) value = "'$value'"; //TODO возможно нужно экранирование
            values.add(value.toString())
        }
        return values
    }

    private fun getFieldsWithoutID(): List<String> {
        return fields.keys.filter { it != "id" }
    }


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

            if (query.getType() == SELECT_ALL) {
                statement.execute(insertQuery)
                val resultSet: ResultSet = statement.resultSet

                val collection = ArrayList<User>()
                while (resultSet.next())
                //TODO: как отойти от привязки к полям класса
                    collection.add(User(
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getInt("id")
                    ))

                queryResult.setCollection(collection)
                resultSet.close()
            } else if (query.getType() == CHECK) {
                var res = ""
                statement.execute(insertQuery)
                val resultSet: ResultSet = statement.resultSet
                while (resultSet.next())
                    res = resultSet.getString("exists")
                resultSet.close()
                statement.close()
                queryResult.setMessage(res)
                return queryResult
            } else statement.execute(insertQuery)

            statement.close()
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
            SELECT_ONE -> selectOneQuery() //не нужен owner
            SELECT_ALL -> selectAllQuery() //получает query без owner
            INSERT -> insertQuery(query)
            UPDATE -> updateQuery(query)
            DELETE -> deleteQuery(query) //не нужен owner
            CHECK -> existsQuery(query)
        }
        return queryString
    }


    private fun selectAllQuery(): String {
        return "SELECT * FROM $tableName;".trimIndent()
    }



    //TODO: доделать по login и id
    private fun selectOneQuery(): String {
        val parameter = "1"
        val s = "id"

        return "SELECT * FROM $tableName WHERE $s=$parameter;".trimIndent()
    }


    private fun insertQuery(query: PostgresQuery): String {
        val values = getFieldValues(query.getUser()).joinToString(", ")
        return "INSERT INTO $tableName VALUES($values);".trimIndent()
    }


    private fun updateQuery(query: PostgresQuery): String {
        val values = getFieldValues(query.getUser())
        val classFields = getFieldsWithoutID()
        var s = ""
        for (i in 0..<classFields.size) {
            s += (classFields[i] + "=" + values[i])
            if (i != classFields.size - 1)
                s += ", "
        }
        return "UPDATE $tableName SET $s WHERE id=${query.getUser().id};"
    }


    private fun deleteQuery(query: PostgresQuery): String {
        return "DELETE FROM $tableName WHERE id=${query.getUser().id};".trimIndent()
    }



    private fun existsQuery(query: PostgresQuery): String {
        val values = getFieldValues(query.getUser())
        val classFields = getFieldsWithoutID()
        var s = ""
        for (i in 0..<classFields.size) {
            s += (classFields[i] + "=" + values[i])
            if (i != classFields.size - 1)
                s += " and "
        }
        return "SELECT EXISTS (SELECT * FROM $tableName WHERE $s);".trimIndent()
    }

    fun closeConnection() {
        connection.close()
        println("closeConnection: OK")
    }

}