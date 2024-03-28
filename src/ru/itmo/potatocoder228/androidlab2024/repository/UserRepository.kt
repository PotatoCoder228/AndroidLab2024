package repository

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.collections.ArrayList
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

import model.User
import dto.PostgresQuery
import dto.PostgresQueryType.*
import config.*

class UserRepository {
    private lateinit var connection: Connection

    lateinit var fields: Map<String, String>

    fun initRepo(): Boolean {
        if (!initConnection()) return false
        getUserFields()
        createUserTable()
        return true
    }

    private fun initConnection(): Boolean {
        try {
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(Config.postgresUrl, Config.postgresUser, Config.postgresPassword)
            return true
        } catch (e: Exception) {
            return false
        }
    }


    fun queryExecutor(query: PostgresQuery, collection: ArrayList<User> = ArrayList<User>()): Boolean {
        if (connection.isClosed) initConnection()
        val statement = connection.createStatement()
        val insertQuery = buildQuery(query)
        val type = query.getType()
        try {
            when (type) {
                SELECT_ALL, SELECT_BY_ID, SELECT_BY_LOGIN -> {
                    statement.execute(insertQuery)
                    val resultSet: ResultSet = statement.resultSet
                    val resultSetMetaData = resultSet.metaData
                    while (resultSet.next()) {
                        val user = User.nullUser
                        for (i in 1..resultSetMetaData.columnCount) {
                            val columnName = resultSetMetaData.getColumnName(i)
                            val columnValue = resultSet.getObject(i)
                            val setterName = "set${columnName.capitalize()}"
                            User::class.java.methods.find { it.name == setterName }?.invoke(user, columnValue)
                        }
                        collection.add(user)
                    }
                    resultSet.close()
                }

                CHECK -> {
                    var res = ""
                    statement.execute(insertQuery)
                    val resultSet: ResultSet = statement.resultSet
                    while (resultSet.next())
                        res = resultSet.getString("exists")
                    resultSet.close()
                    statement.close()
                    return res == "t"
                }

                else -> statement.execute(insertQuery)
            }
            statement.close()
            return true
        } catch (e: Exception) {
            return false
        }
    }


    private fun buildQuery(query: PostgresQuery): String {
        val queryString: String = when (query.getType()) {
            CHECK -> existsQuery(query)
            SELECT_ALL -> selectAllQuery()
            SELECT_BY_ID -> selectByIdQuery(query)
            SELECT_BY_LOGIN -> selectByLoginQuery(query)
            INSERT -> insertQuery(query)
            UPDATE -> updateQuery(query)
            DELETE_BY_ID -> deleteByIdQuery(query)
            DELETE_BY_LOGIN -> deleteByLoginQuery(query)
        }
        return queryString
    }


    private fun selectAllQuery(): String {
        return "SELECT * FROM ${Config.postgresTableName};".trimIndent()
    }


    private fun selectByIdQuery(query: PostgresQuery): String {
        return "SELECT * FROM ${Config.postgresTableName} WHERE id=${query.getUser().id};".trimIndent()
    }

    private fun selectByLoginQuery(query: PostgresQuery): String {
        return "SELECT * FROM ${Config.postgresTableName} WHERE login='${query.getUser().login}';".trimIndent()
    }


    private fun insertQuery(query: PostgresQuery): String {
        val fieldsWithoutId = getFieldsWithoutID().joinToString(", ")
        val values = getFieldValues(query.getUser()).joinToString(", ")
        return "INSERT INTO ${Config.postgresTableName}(" + fieldsWithoutId + ") VALUES($values);".trimIndent()
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
        return "UPDATE ${Config.postgresTableName} SET $s WHERE id=${query.getUser().id};"
    }


    private fun deleteByIdQuery(query: PostgresQuery): String {
        return "DELETE FROM ${Config.postgresTableName} WHERE id=${query.getUser().id};".trimIndent()
    }


    private fun deleteByLoginQuery(query: PostgresQuery): String {
        return "DELETE FROM ${Config.postgresTableName} WHERE login='${query.getUser().login}';".trimIndent()
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
        return "SELECT EXISTS (SELECT * FROM ${Config.postgresTableName} WHERE $s);".trimIndent()
    }

    fun closeConnection() {
        connection.close()
        println("closeConnection: OK")
    }


    private fun translateToPostgresDT(type: KType): String {
        return when (type.classifier) {
            Int::class -> "INT"
            Long::class -> "BIGINT"
            String::class -> "TEXT"
            else -> throw Exception("Неопознанный тип данных")
        }
    }

    private fun getUserFields() {
        fields = User::class.memberProperties.associate { it.name to translateToPostgresDT(it.returnType) }
    }


    private fun createUserTable(): Boolean {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS ${Config.postgresTableName} (" +
                getStringFieldsPairsStructure() +
                ");".trimIndent()
        val statement = connection.createStatement()
        try {
            statement.execute(createTableQuery)
            println("createUserTable: OK")
            return true
        } catch (e: Exception) {
            println("createUserTable: ${e.message}")
            return false
        }
    }

    private fun getStringFieldsPairsStructure(): String {
        return fields.entries.joinToString(", ") { entry ->
            val key = entry.key
            val value = if (key == "id") "BIGSERIAL" else entry.value
            val primaryKey = if (key == "id") "primary key" else ""
            "$key $value $primaryKey"
        }
    }


    private fun getFieldValues(user: User): List<String> {
        val values = mutableListOf<String>()
        val fieldNames = fields.keys.toList()
        for (fieldName in fieldNames) {
            if (fieldName == "id") continue
            var value = user::class.memberProperties.first { it.name == fieldName }.call(user)
            if (fields[fieldName].equals("String")) value = "'$value'"
            values.add(value.toString())
        }
        return values
    }

    private fun getFieldsWithoutID(): List<String> {
        return this.fields.keys.filter { it != "id" }
    }


}