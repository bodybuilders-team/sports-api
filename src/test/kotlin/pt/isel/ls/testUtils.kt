package pt.isel.ls

import java.io.File
import java.sql.Connection
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

	while (rs.next().also {
				assertEquals(it, mockTableIt.hasNext())
			}) {
		val mockRow = mockTableIt.next()

		assertEquals(mockRow.size, rs.metaData.columnCount)

		rowAssertionCb(mockRow, rs)
	}
}

/**
 * Runs SQL script
 * @param filepath path of the script to run
 */
fun Connection.runScript(filepath: String) {
	File(filepath)
			.readText()
			.also {
				this.prepareStatement(it)
						.executeUpdate()
			}
}
