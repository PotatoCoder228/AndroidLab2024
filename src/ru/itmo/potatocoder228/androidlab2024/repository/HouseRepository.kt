package repository

import model.House
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class HouseRepository {
    private lateinit var connection : Connection
    private lateinit var fields : Map<String, String>

    fun initDB() : Boolean {
        if(!initConnection()) return false;
        getHouseFields();
        createHouseTable();
        return true;
    }
    private fun initConnection() : Boolean {
        val url = "jdbc:clickhouse://localhost:8123/default"
        val properties = Properties()

        properties["user"] = "default"
        properties["password"] = "password"

        try {
            // Устанавливаем путь к ClickHouse JDBC драйверу
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver")
            // Устанавливаем соединение с базой данных
            connection = DriverManager.getConnection(url, properties)

            println("Успешное подключение к базе данных ClickHouse!")
            return true
        } catch (e: Exception) {
            println("Ошибка при подключении к базе данных ClickHouse: ${e.message}")
            return false
        }
    }

    private fun translateToClickhouseDT(type: KType): String {
        return when (type.classifier) {
            Int::class -> "Int32"
            Long::class -> "Int64"
            String::class -> "String"
            Boolean::class -> "Boolean"
            else -> throw Exception("Неопознанный тип данных")
        }
    }

    private fun translateToKotlinType(type: String) : String {
        return when(type) {
            "Int32" -> "Int"
            "Int64" -> "Long"
            else -> type
        }
    }


    /*
        Получить все поля и типы данных для класса House;
     */
    private fun getHouseFields(){
        fields = House::class.memberProperties.associate { it.name to translateToClickhouseDT(it.returnType) }
    }

    /*
        Получить строковое представление всех названий полей и типов данных для класса House;
        Используется для CREATE TABLE:
     */
    private fun getStringFieldsPairsStructure(): String {
        return fields.entries.joinToString(", ") { entry ->
            val key = entry.key
            val value = entry.value
            val primaryKey = if (key == "id") "primary key" else ""
            "$key $value $primaryKey"
        }
    }

    /*
        Получить строковое представление всех названий кроме id для класса House;
        Используется для INSERT INTO:
     */
    private fun getStringFieldsWithoutID(): String {
        return fields.keys.filter { it != "id" }.joinToString(", ")
    }

    private fun getStringFields(): String {
        return fields.keys.joinToString(", ")
    }
    /*
        Получить значения всех полей кроме id в строковом формате;
        Используется для INSERT INTO:
     */
    private fun getFieldValues(house: House) : String {
        val values = mutableListOf<String>()
        val fieldNames = fields.keys.toList()
        for (fieldName in fieldNames) {
            //if(fieldName == "id") continue;
            var value = house::class.memberProperties.first { it.name == fieldName }.call(house)
            if(fields[fieldName].equals("String")) value = "'$value'"; //TODO возможно нужно экранирование
            values.add(value.toString())
        }
        return values.joinToString(", ")
    }



    private fun createHouseTable() : Boolean {
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS HOUSE (""" +
                getStringFieldsPairsStructure() +
        """) ENGINE = MergeTree()
        ORDER BY (id)""".trimIndent()

        val statement = connection.createStatement()
        try{
            statement.execute(createTableQuery)
            println("Таблица HOUSE успешно создана в базе данных!")
            return true
        } catch (e : Exception){
            println("Таблица HOUSE не создана в базе данных!")
            return false
        }
    }

    fun saveHouse(house : House) {
        val fieldsWithoutId = getStringFields();
        val insertQuery = """
        INSERT INTO HOUSE (""" + fieldsWithoutId + """)
        VALUES (""" + getFieldValues(house) + """)
    """.trimIndent()
        val statement = connection.createStatement()
        try{
            statement.execute(insertQuery)
            println("Новая запись успешно добавлена в таблицу HOUSE!")
        }catch (e : Exception){
            println("Новая запись НЕ добавлена в таблицу HOUSE!")
        }
    }

    fun updateHouse(house: House){ //TODO update = delete + insert
        println("House is updated " + house);
    }
    fun findByUserId(id: Long) : House{
        val selectQuery = """
            SELECT * FROM HOUSE WHERE hostId = """ + id.toString().trimIndent()
        println(selectQuery)

        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(selectQuery)

        if (resultSet.next()) {
            //val constructorParams = fields.values.joinToString(", ") {
                //"resultSet.get${it}(\"${fields.keys.filter { key -> key == translateToKotlinType(it) }}\")"
            //}

            val codeToEvaluate = "House($constructorParams)"
            println("Generated code to evaluate: $codeToEvaluate")

            /*val clazz = Class.forName("model.House")
            val constructor = clazz.getConstructor(*fieldAndType.values.map { String::class.java }.toTypedArray())

            return constructor.newInstance(*fieldAndType.values.map { eval(it) }.toTypedArray()) as House*/
        } else {
            throw IllegalStateException("House not found for user with id $id")
        }
        return House(1, "a", false, 3);
    }

    fun findById(id: Long) : House{
        println("House is got"); //TODO
        return House(1, "a", false, 3);

    }

    fun closeConnection() {
        connection.close()
    }

    fun uuidToInt(uuid: UUID): Long {
        return (uuid.mostSignificantBits and Long.MAX_VALUE) or (uuid.leastSignificantBits and Long.MAX_VALUE)
    }

    fun intToUUID(intVal: Long): UUID {
        return UUID(intVal, Long.MIN_VALUE)
    }


}