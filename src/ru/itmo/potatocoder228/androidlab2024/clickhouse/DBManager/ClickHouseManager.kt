import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
class ClickHouseManager {

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

    fun createExampleTable() : Boolean {
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS MY_LOG (
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

    fun createExampleQuery() : Boolean{
        val insertQuery = """
        INSERT INTO MY_LOG (id, house_id, gadget_id, timestamp)
        VALUES (1, 123, 456, '2022-01-01 10:00:00')
    """.trimIndent()

        val statement = connection.createStatement()
        try{
            statement.execute(insertQuery)
            println("Новая запись успешно добавлена в таблицу MY_LOG!")
            return true
        }catch (e : Exception){
            println("Новая запись НЕ добавлена в таблицу MY_LOG!")
            return false
        }
    }

    fun closeConnection() {
        connection.close()
    }
}