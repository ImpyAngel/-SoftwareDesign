package ru.akirakozov.sd.refactoring

import java.lang.IllegalStateException
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
data class Product(val name: String, val price: Int)

class DBLayer {

    private fun <T> dbQuery(block: Statement.() -> T): T =
        DriverManager.getConnection("jdbc:sqlite:test.db").use { c ->
            val stmt = c.createStatement()
            val ans = block(stmt)
            stmt.close()
            ans
        }

    private fun executeUpdate(sql: String) = dbQuery {
        executeUpdate(sql)
    }

    private fun <T> executeQuery(sql: String, block: ResultSet.() -> T): T = dbQuery {
        val resultSet = executeQuery(sql)
        resultSet.use(block)
    }

    fun tryCreate() = executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)")

    private fun ResultSet.toEntities(): List<Product> {
        val ans = mutableListOf<Product>()
        while (next()) {
            val name = getString("name")
            val price = getInt("price")
            ans += Product(name, price)
        }
        return ans
    }

    private fun ResultSet.getSingleInt(): Int {
        if (!next()) throw IllegalStateException()
        return getInt(1)
    }

    fun cleanUp() {
        executeUpdate("DELETE FROM PRODUCT")
    }

    fun add(name: String, price: Int) = executeUpdate("INSERT INTO PRODUCT " +
            "(NAME, PRICE) VALUES (\"$name\",$price)")

    fun all() = executeQuery("SELECT * FROM PRODUCT") {
        toEntities()
    }

    fun min() = executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1") {
        toEntities().single()
    }

    fun max() = executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1") {
        toEntities().single()
    }

    fun sum() = executeQuery("SELECT SUM(price) FROM PRODUCT") {
        getSingleInt()
    }

    fun count() = executeQuery("SELECT COUNT(*) FROM PRODUCT") {
        getSingleInt()
    }
}
