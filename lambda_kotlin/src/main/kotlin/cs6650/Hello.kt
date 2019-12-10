package cs6650

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement
import java.util.*


data class PathParams(val skierID: Int, val resortID: Int, val seasonID: String, val dayID: String)
data class Body(val liftID: Int, val time: Int)
data class HandlerInput(val pathParameters: PathParams, val body: String)
data class HandlerOutput(val body: String, val statusCode: Int, val headers: Map<String, String>)
data class DBSecret(val username: String, val host: String, val password: String, val port: Int, val engine: String, val dbInstanceIdentifier: String)

object MySQLConnection {
    val conn: Connection
    init {
        //val secret = SecretAPI.getSecret()
        val props = Properties()
        print("about to get secret")
        //val secret = jacksonObjectMapper().readValue<DBSecret>(SecretAPI.getSecret())
        print("got secret")
        props["user"] = System.getenv("username")
        props["password"] = System.getenv("password")
        props["timezone"] = "UTC"
        //print(secret.username)
        Class.forName("com.mysql.cj.jdbc.Driver")
        conn = DriverManager.getConnection("jdbc:mysql://" + System.getenv("host") + ":" + System.getenv("port") + "/" + "Upik"
                + "?useSSL=false", props)
    }
}

class Main {
    val mapper = jacksonObjectMapper()

    fun handler(input: InputStream, output: OutputStream): Unit {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        val inputObj = mapper.readValue<HandlerInput>(input)
        print(inputObj)
        val body = mapper.readValue<Body>(inputObj.body)

        val headers = HashMap<String, String>()
        //headers["Content-Type"] = "application/json"
        headers["X-Custom-Header"] = "application/json"

        val conn = MySQLConnection.conn

        val insert =
            "INSERT INTO LiftRides(ResortID, SeasonID, DayID, SkierID, LiftID, Time, Vertical) VALUES" +
                    "(?,?,?,?,?,?,?)"
        val stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)

        try {
            stmt.setInt(1, inputObj.pathParameters.resortID)
            stmt.setString(2, inputObj.pathParameters.seasonID)
            stmt.setString(3, inputObj.pathParameters.dayID)
            stmt.setInt(4, inputObj.pathParameters.skierID)
            stmt.setInt(5, body.liftID)
            stmt.setInt(6, body.time)
            stmt.setInt(7, body.liftID*10)
            stmt.executeUpdate()
        } catch (e: SQLIntegrityConstraintViolationException) {
            mapper.writeValue(output, HandlerOutput("Duplicate primary key, did not write", 400, headers))
        } finally {
            stmt.close()
        }

        mapper.writeValue(output, HandlerOutput("posted", 200, headers))
    }
}


