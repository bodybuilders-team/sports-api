package pt.isel.ls

import org.http4k.core.Request
import java.sql.ResultSet
import kotlin.test.assertEquals

/**
 * Traverses both mock and real tables while asserting that
 * number of rows and columns are the same.
 * @param mockTable MockTable
 * @param rs Cursor containing table data
 * @param rowAssertionCb Callback for assertion of mock and real row data
 */
fun tableAsserter(
    mockTable: Array<Array<Any>>,
    rs: ResultSet,
    rowAssertionCb: (Array<Any>, ResultSet) -> Unit
) {
    val mockTableIt = mockTable.iterator()

    while (rs.next().also { assertEquals(it, mockTableIt.hasNext()) }) {
        val mockRow = mockTableIt.next()

        assertEquals(mockRow.size, rs.metaData.columnCount)

        rowAssertionCb(mockRow, rs)
    }
}

/**
 * Sets body of the request as JSON with given data and content type header
 * @param requestBody JSON data
 * @return Request with JSON body
 */
fun Request.json(requestBody: String) =
    this.header("Content-Type", "application/json")
        .body(requestBody)

/**
 * Set request bearer token in authorization header
 * @param token bearer token
 * @return Request with bearer token
 */
fun Request.token(token: String): Request =
    this.header("Authorization", "Bearer $token")
