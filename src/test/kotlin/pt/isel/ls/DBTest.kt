package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource
import java.sql.ResultSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Traverses both mock and real tables while asserting
 * number of rows and columns are the same.
 * @param mockTable MockTable
 * @param rs Cursor containing table data
 * @param rowAssertionCb Callback for assertion of mock and real row data
 */
private fun tableAsserter(
    mockTable: Array<Array<Any>>,
    rs: ResultSet,
    rowAssertionCb: (Array<Any>, ResultSet) -> Unit
) {

    val mockTableIt = mockTable.iterator()

    while (rs.next().also {
            assertEquals(it, mockTableIt.hasNext())
        }) {
        val mockRow = mockTableIt.next()
        //Checked every iteration so mockTable does not
        // have variable number of columns
        assertEquals(mockRow.size, rs.metaData.columnCount)

        rowAssertionCb(mockRow, rs)
    }
}

class DBTest {
    companion object {
        val dataSource = PGSimpleDataSource().apply {
            val jdbcDatabaseURL: String = System.getenv("JDBC_DATABASE_URL")
            this.setURL(jdbcDatabaseURL)
        }
    }

    @Test
    fun `Retrieve All Students`() {
        dataSource.connection.use { conn ->
            val stm = conn.prepareStatement("SELECT * FROM students")
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(12345, "Alice", 1),
                arrayOf(12346, "Bob", 1)
            )

            tableAsserter(mockTable, rs)
            { mockRow, row ->

                assertEquals(mockRow[0], row.getInt("number"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getInt("course"))
            }

        }
    }

    @Test
    fun `Retrieve All Courses`() {
        dataSource.connection.use {
            val stm = it.prepareStatement("select * from courses")
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(1, "LEIC")
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("cid"))
                assertEquals(mockRow[1], row.getString("name"))
            }
        }
    }

    @Test
    fun `Retrieve Courses CID sequence`() {
        dataSource.connection.use {
            val stm = it.prepareStatement("select * from courses_cid_seq")
            val rs = stm.executeQuery()
            assertTrue(rs.next())
            assertEquals(1, rs.getInt("last_value"))
        }
    }
}