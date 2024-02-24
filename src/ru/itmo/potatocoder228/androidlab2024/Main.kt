
fun main() {
    println("Hello, world!")
    val clickHouseManager = ClickHouseManager()
    clickHouseManager.initConnection()
    clickHouseManager.createExampleTable()
    clickHouseManager.createExampleQuery()
    clickHouseManager.closeConnection()


}