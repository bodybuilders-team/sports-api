package pt.isel.ls.unit.services

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersServicesTests : AppServicesTests() {
    // createNewUser
    @Test
    fun `createNewUser creates user correctly in the database`(): Unit = db.execute { conn ->
        val createUserResponse = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(
            User(createUserResponse.uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"),
            db.users.getUser(conn, createUserResponse.uid)
        )
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val createUserResponse1 = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val createUserResponse2 = services.users.createNewUser("André Jesus", "andrejesus@mail.com")
        val createUserResponse3 = services.users.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, createUserResponse1.uid)
        assertEquals(2, createUserResponse2.uid)
        assertEquals(3, createUserResponse3.uid)
    }

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(user, services.users.getUser(1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`() {

        assertFailsWith<NotFoundException> {
            services.users.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->

        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        assertEquals(listOf(user1, user2, user3), services.users.getAllUsers(0, 10).users)
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {

        assertEquals(emptyList(), services.users.getAllUsers(0, 10).users)
    }

    @Test
    fun `createNewRoute creates route correctly in the database`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(conn, rid))
    }

// getUserActivities

    @Test
    fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        val activities = services.users.getUserActivities(1, 0, 10).activities

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }
}
