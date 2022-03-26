package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.SportsPostgres
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsPostgresTests {

    companion object {
        val dataSource = PGSimpleDataSource().apply {
            val jdbcDatabaseURL: String = System.getenv("JDBC_DATABASE_URL")
            this.setURL(jdbcDatabaseURL)
        }
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
    fun `createNewUser creates user correctly in the database`() {
        val uid = SportsPostgres.createNewUser("Paulão", "paulao@mail.com")

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
    fun `getUser returns the user object`() {
        val user = SportsPostgres.getUser(1)

        assertEquals(User(1, "André Jesus", "A48280@alunos.isel.pt"), user)
    }

    @Test
    fun `getUser throws SportsError (Not found) if the user with the uid doesn't exist`() {
        assertFailsWith<SportsError> {
            SportsPostgres.getUser(0)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {
        val users = listOf(
            User(1, "André Jesus", "A48280@alunos.isel.pt"),
            User(2, "André Páscoa", "A48089@alunos.isel.pt"),
            User(3, "Nyckollas Brandão", "A48287@alunos.isel.pt")
        )

        assertEquals(users, SportsPostgres.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), SportsPostgres.getAllUsers())
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`() {
        val token = SportsPostgres.createUserToken(1)

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
    fun `getUID returns the uid correctly`() {
        val uid = SportsPostgres.getUID("49698b60-12ca-4df7-8950-d783124f5fas")
        assertEquals(1, uid)
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`() {
        assertFailsWith<SportsError> {
            SportsPostgres.getUID("T-o-k-e-n")
        }
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
        val rid = SportsPostgres.createNewRoute("Odivelas", "Chelas", 150, 1)

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
    fun `getRoute returns the route object`() {
        val route = SportsPostgres.getRoute(1)
        assertEquals(Route(1, "Odivelas", "Chelas", 150, 1), route)
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {
        assertFailsWith<SportsError> {
            SportsPostgres.getRoute(0)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {
        val routes = listOf(
            Route(1, "Odivelas", "Chelas", 150, 1),
            Route(2, "Chelas", "Odivelas", 150, 2),
            Route(3, "Lisboa", "Porto", 1500, 3)
        )

        assertEquals(routes, SportsPostgres.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), SportsPostgres.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
        val sid = SportsPostgres.createNewSport("Badminton", "idk", 1)

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
    fun `getSport returns the sport object`() {
        val sport = SportsPostgres.getSport(1)
        assertEquals(Sport(1, "Soccer", "Kick a ball to score a goal", 1), sport)
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {
        assertFailsWith<SportsError> {
            SportsPostgres.getSport(0)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`() {
        val sports = listOf(
            Sport(1, "Soccer", "Kick a ball to score a goal", 1),
            Sport(2, "Powerlifting", "Get big", 2),
            Sport(3, "Basketball", "Shoot a ball through a hoop", 3)
        )

        assertEquals(sports, SportsPostgres.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        dataSource.connection.use {
            it.runScript("src/main/sql/cleanData.sql")
        }
        assertEquals(emptyList(), SportsPostgres.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {
        val aid = SportsPostgres.createNewActivity("2022-11-05", "14:66:27.903", 1, 1, 1)

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM activities WHERE id = ?")
            stm.setInt(1, aid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(2, "2022-11-05", "14:66:27.903", 1, 1, 1)
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
    fun `getActivity returns the activity object`() {
        val activity = SportsPostgres.getActivity(1)
        assertEquals(Activity(1, "2022-11-20", "72:44:63.903", 1, 2, 1), activity)
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {
        assertFailsWith<SportsError> {
            SportsPostgres.getActivity(0)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`() {
        SportsPostgres.deleteActivity(1)

        assertFailsWith<SportsError> {
            SportsPostgres.getActivity(1)
        }
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`() {
        val activities = SportsPostgres.getSportActivities(2)
        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 2, 1)), activities)
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`() {
        val activities = SportsPostgres.getUserActivities(1)
        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 2, 1)), activities)
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`() {
        val activities = SportsPostgres.getActivities(sid = 2, "descending", "2022-11-20", rid = 1)
        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 2, 1)), activities)
    }

    // TODO: 26/03/2022 Add more tests (synchronize with SportDataMemTests?)
}
