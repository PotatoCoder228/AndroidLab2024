package config
object Config {
    const val secret = "secret"
    const val issuer = "http://localhost:8080/"
    const val audience = "http://localhost:8080/page"
    const val myRealm = "Access to 'page'"
    const val port = 8080
    const val host = "127.0.0.1"

    //postgres connection
    const val postgresUrl = "jdbc:postgresql://localhost/postgres"
    const val postgresUser = "postgres"
    const val postgresPassword = ""
    const val postgresTableName = "OWNER"
}