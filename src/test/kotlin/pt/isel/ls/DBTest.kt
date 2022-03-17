package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PSQLException
import kotlin.test.*


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
			val stm = it.prepareStatement("SELECT * FROM courses")
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
			val stm = it.prepareStatement("SELECT * FROM courses_cid_seq")
			val rs = stm.executeQuery()
			assertTrue(rs.next())
			assertEquals(1, rs.getInt("last_value"))
		}
	}

	@Test
	fun `Update course name`() {
		dataSource.connection.use {
			val stm = it.prepareStatement("UPDATE courses SET name = \'MEIC\' WHERE cid = 1")
			val result = stm.executeUpdate()
			assertEquals(1, result)

			val stm2 = it.prepareStatement("SELECT * FROM courses")
			val rs = stm2.executeQuery()
			rs.next()

			assertEquals("MEIC", rs.getString("name"))
		}
	}

	@Test
	fun `Update student name`() {
		dataSource.connection.use {
			val stm = it.prepareStatement("UPDATE students SET name = \'bolinha\' WHERE number = 12345")
			val result = stm.executeUpdate()
			assertEquals(1, result)

			val stm2 = it.prepareStatement("SELECT * FROM students WHERE number = 12345")
			val rs = stm2.executeQuery()
			rs.next()

			assertEquals("bolinha", rs.getString("name"))
		}
	}

	@Test
	fun `Insert new course`() {
		dataSource.connection.use {
			it.prepareStatement("INSERT INTO courses(name) VALUES (\'MEIC\')")
					.executeUpdate()

			val stm2 = it.prepareStatement("SELECT * FROM courses WHERE name = \'MEIC\'")
			val rs = stm2.executeQuery()
			rs.next()

			assertEquals("MEIC", rs.getString("name"))
		}
	}

	@Test
	fun `Insert new student`() {
		dataSource.connection.use {
			it.prepareStatement("INSERT INTO students VALUES (12350, \'Jorge\', 1)")
					.executeUpdate()

			val stm2 = it.prepareStatement("SELECT * FROM students WHERE name = \'Jorge\'")
			val rs = stm2.executeQuery()
			rs.next()

			assertEquals("Jorge", rs.getString("name"))
		}
	}


	@Test
	fun `Changing a course cid to next sequence value throws exception after insertion of a new course`() {
		dataSource.connection.use {
			assertFailsWith<PSQLException> {
				it.prepareStatement("INSERT INTO courses(name) VALUES (\'bolinha\')")
						.executeUpdate()

				val result = it.prepareStatement("UPDATE courses SET cid = 3 WHERE cid = 2")
						.executeUpdate()
				assertEquals(1, result)

				it.prepareStatement("INSERT INTO courses(name) VALUES (\'bolinha\')")
						.executeUpdate()
			}
		}
	}

	@Test
	fun `Next insertion in courses will have cid equal to last_value of Courses CID sequence + 1`() {
		dataSource.connection.use {
			it.prepareStatement("INSERT INTO courses(name) VALUES (\'bolinha\')")
					.executeUpdate()

			it.prepareStatement("INSERT INTO courses(name) VALUES (\'bolinha2\')")
					.executeUpdate()

			it.prepareStatement("INSERT INTO courses(name) VALUES (\'bolinha3\')")
					.executeUpdate()

			val stm2 = it.prepareStatement("SELECT last_value FROM courses_cid_seq")
			val rs = stm2.executeQuery()
			rs.next()

			assertEquals(4, rs.getInt("last_value"))
		}
	}

	@BeforeTest
	fun setupDatabase() {
		dataSource.connection.use {
			it.runScript("src/main/sql/createSchema.sql")
			it.runScript("src/main/sql/addData.sql")
		}
	}
}
