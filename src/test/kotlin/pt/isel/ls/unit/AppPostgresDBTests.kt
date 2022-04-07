package pt.isel.ls.unit

import kotlinx.datetime.toLocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.integration.IntegrationTests
import pt.isel.ls.runScript
import pt.isel.ls.sports.JDBC_DATABASE_URL_ENV
import pt.isel.ls.sports.database.AppPostgresDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.tableAsserter
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppPostgresDBTests {

    companion object {
        private val jdbcDatabaseURL: String = System.getenv(JDBC_DATABASE_URL_ENV)
        private val dataSource = PGSimpleDataSource().apply { setURL(jdbcDatabaseURL) }
        private val db = AppPostgresDB(jdbcDatabaseURL)
    }

    @BeforeTest
    fun setupDatabase() {
        dataSource.connection.use {
            it.runScript("src/main/sql/createSchema.sql")
            it.runScript("src/main/sql/addData.sql")
        }
    }

    // createNewUser

    @Test
    fun `createNewUser creates user correctly in the database`(): Unit = db.connection.use { conn ->
        val uid = db.users.createNewUser(conn, "Paulão", "paulao@mail.com")

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM users WHERE id = ?")
            stm.setInt(1, uid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Paulão", "paulao@mail.com")
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getString("email"))
            }
        }
    }

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = IntegrationTests.db.connection.use { conn ->
        val user = db.users.getUser(conn, 1)

        assertEquals(User(1, "André Jesus", "A48280@alunos.isel.pt"), user)
    }

    @Test
    fun `getUser throws SportsError (Not found) if the user with the uid doesn't exist`(): Unit =
        IntegrationTests.db.connection.use { conn ->
            assertFailsWith<AppError> {
                db.users.getUser(conn, 0)
            }
        }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.connection.use { conn ->
        val users = listOf(
            User(1, "André Jesus", "A48280@alunos.isel.pt"),
            User(2, "André Páscoa", "A48089@alunos.isel.pt"),
            User(3, "Nyckollas Brandão", "A48287@alunos.isel.pt")
        )

        assertEquals(users, db.users.getAllUsers(conn))
    }

    @Test
    fun `getAllUsers with no created users returns empty list`(): Unit = db.connection.use { conn ->
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), db.users.getAllUsers(conn))
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`(): Unit = db.connection.use { conn ->
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM tokens WHERE token = ?")
            stm.setString(1, token)
            val rs = stm.executeQuery().also { rs -> rs.next() }

            assertEquals(token, rs.getString(1))
            assertEquals(1, rs.getInt(2))
        }
    }

    // getUID

    @Test
    fun `getUID returns the uid correctly`(): Unit = db.connection.use { conn ->
        val uid = db.tokens.getUID(conn, "49698b60-12ca-4df7-8950-d783124f5fas")
        assertEquals(1, uid)
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`(): Unit =
        db.connection.use { conn ->
            assertFailsWith<AppError> {
                db.tokens.getUID(conn, "T-o-k-e-n")
            }
        }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`(): Unit = db.connection.use { conn ->
        val rid = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 150, 1)

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM routes WHERE id = ?")
            stm.setInt(1, rid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Odivelas", "Chelas", 150, 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("start_location"))
                assertEquals(mockRow[2], row.getString("end_location"))
                assertEquals(mockRow[3], row.getInt("distance"))
                assertEquals(mockRow[4], row.getInt("uid"))
            }
        }
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`(): Unit = db.connection.use { conn ->
        val route = db.routes.getRoute(conn, 1)
        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), route)
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`(): Unit =
        db.connection.use { conn ->
            assertFailsWith<AppError> {
                db.routes.getRoute(conn, 0)
            }
        }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`(): Unit = db.connection.use { conn ->
        val routes = listOf(
            Route(1, "Odivelas", "Chelas", 0.15, 1),
            Route(2, "Chelas", "Odivelas", 0.15, 2),
            Route(3, "Lisboa", "Porto", 1.5, 3)
        )

        assertEquals(routes, db.routes.getAllRoutes(conn))
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`(): Unit = db.connection.use { conn ->
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), db.routes.getAllRoutes(conn))
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`(): Unit = db.connection.use { conn ->
        val sid = db.sports.createNewSport(conn, 1, "Badminton", "idk")

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM sports WHERE id = ?")
            stm.setInt(1, sid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Badminton", "idk", 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getString("description"))
                assertEquals(mockRow[3], row.getInt("uid"))
            }
        }
    }

    // getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.connection.use { conn ->
        val sport = db.sports.getSport(conn, 1)
        assertEquals(Sport(1, "Soccer", 1, "Kick a ball to score a goal"), sport)
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`(): Unit =
        db.connection.use { conn ->
            assertFailsWith<AppError> {
                db.sports.getSport(conn, 0)
            }
        }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`(): Unit = db.connection.use { conn ->
        val sports = listOf(
            Sport(1, "Soccer", 1, "Kick a ball to score a goal"),
            Sport(2, "Powerlifting", 2, "Get big"),
            Sport(3, "Basketball", 3, "Shoot a ball through a hoop")
        )

        assertEquals(sports, db.sports.getAllSports(conn))
    }

    @Test
    fun `getAllSports with no created sports returns empty list`(): Unit = db.connection.use { conn ->
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), db.sports.getAllSports(conn))
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`(): Unit = db.connection.use { conn ->
        val aid =
            db.activities.createNewActivity(conn, 1, "2022-11-05".toLocalDate(), "14:59:27.903".toDuration(), 1, 1)

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM activities WHERE id = ?")
            stm.setInt(1, aid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "2022-11-05", "14:59:27.903", 1, 1, 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getDate("date").toString())
                assertEquals(mockRow[2], row.getString("duration"))
                assertEquals(mockRow[3], row.getInt("uid"))
                assertEquals(mockRow[4], row.getInt("sid"))
                assertEquals(mockRow[5], row.getInt("rid"))
            }
        }
    }

    // getActivity

    @Test
    fun `getActivity returns the activity object`(): Unit = db.connection.use { conn ->
        val activity = db.activities.getActivity(conn, 1)
        assertEquals(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null), activity)
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppError> {
                db.activities.getActivity(conn, 0)
            }
        }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`(): Unit = db.connection.use { conn ->
        db.activities.deleteActivity(conn, 1)

        assertFailsWith<AppError> {
            db.activities.getActivity(conn, 1)
        }
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`(): Unit = db.connection.use { conn ->
        val activities = db.activities.getSportActivities(conn, 1)
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`(): Unit = db.connection.use { conn ->
        val activities = db.activities.getUserActivities(conn, 1)
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }

    // getActivities

    @Test
    fun `getActivities with descending order returns the activities list`(): Unit = db.connection.use { conn ->
        val activities =
            db.activities.getActivities(conn, sid = 2, SortOrder.DESCENDING, "2022-11-21".toLocalDate(), rid = 1)

        val mockActivities = listOf(
            Activity(
                id = 3,
                date = "2022-11-21".toLocalDate(),
                duration = "20:23:55.263".toDuration(),
                uid = 3,
                sid = 2,
                rid = 1
            ),
            Activity(
                id = 2,
                date = "2022-11-21".toLocalDate(),
                duration = "10:10:10.100".toDuration(),
                uid = 2,
                sid = 2,
                rid = 1
            )
        )
        assertEquals(mockActivities, activities)
    }

    @Test
    fun `getActivities with ascending order returns the activities list`(): Unit = db.connection.use { conn ->
        val activities =
            db.activities.getActivities(conn, sid = 2, SortOrder.ASCENDING, "2022-11-21".toLocalDate(), rid = 1)

        val mockActivities = listOf(
            Activity(
                id = 2,
                date = "2022-11-21".toLocalDate(),
                duration = "10:10:10.100".toDuration(),
                uid = 2,
                sid = 2,
                rid = 1
            ),
            Activity(
                id = 3,
                date = "2022-11-21".toLocalDate(),
                duration = "20:23:55.263".toDuration(),
                uid = 3,
                sid = 2,
                rid = 1
            )

        )
        assertEquals(mockActivities, activities)
    }

    // TODO: 26/03/2022 Add more tests (synchronize with SportDataMemTests?)
    // TODO: 06/04/2022 Add tests for db.execute transactions error handling
}
