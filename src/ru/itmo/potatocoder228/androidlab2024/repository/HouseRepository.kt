package repository

import model.House
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import kotlin.reflect.full.memberProperties

class HouseRepository {
    private lateinit var connection : Connection
    fun initConnection() : Boolean {
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

    fun getHouseFields(){
        val fieldNamesWithType = House::class.memberProperties.map { "${it.name} ${it.returnType}" }
        println(fieldNamesWithType.joinToString())
    }

    fun createHouseTable() : Boolean {
        
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS HOUSE (
            id UInt32,
            house_id UInt32,
            gadget_id UInt32,
            timestamp DateTime
        ) ENGINE = MergeTree()
        ORDER BY (id)
    """.trimIndent()

        val statement = connection.createStatement()
        try{
            statement.execute(createTableQuery)
            println("Таблица MY_LOG успешно создана в базе данных!")
            return true
        } catch (e : Exception){
            println("Таблица MY_LOG не создана в базе данных!")
            return false
        }
    }

    fun saveHouse(house : House) {
        println("House is saved " + house);
    }

    fun updateHouse(house: House){
        println("House is updated " + house);
    }
    fun findByUserId(id: Int) : House{
        println("House is got");
        return House(1, "a", false, 3);
    }

    fun findById(id: Int) : House{
        println("House is got");
        return House(1, "a", false, 3);

    }

}