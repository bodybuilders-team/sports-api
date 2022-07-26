package pt.isel.ls.sports.unit.services.sections.users

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.services.AbstractServicesTests
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersServicesTests : AbstractServicesTests() {

    // createNewUser

    @Test
    fun `createNewUser creates user correctly`(): Unit = db.execute { conn ->
        val uid = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com", "1234")

        assertEquals(
            User(
                uid,
                "Nyckollas Brandão",
                "nyckollasbrandao@mail.com",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            db.users.getUser(conn, uid)
        )
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val uid1 = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com", "1234")
        val uid2 = services.users.createNewUser("André Jesus", "andrejesus@mail.com", "1234")
        val uid3 = services.users.createNewUser("André Páscoa", "andrepascoa@mail.com", "1234")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    @Test
    fun `createNewUser throws InvalidArgumentException if the name is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.createNewUser("Ny", "nyckollasbrandao@mail.com", "1234")
        }
    }

    @Test
    fun `createNewUser throws InvalidArgumentException if the email is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.createNewUser("Nyckollas Brandão", "@@@@mail.com", "1234")
        }
    }

    @Test
    fun `createNewUser throws AlreadyExistsException if a user with that email already exists`(): Unit =
        db.execute { conn ->
            db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

            assertFailsWith<AlreadyExistsException> {
                services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com", "1234")
            }
        }

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS"), services.users.getUser(uid))
    }

    @Test
    fun `getUser throws InvalidArgumentException if the uid is not positive`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.getUser(-5)
        }
    }

    @Test
    fun `getUser throws NotFoundException if the user with the uid doesn't exist`() {
        assertFailsWith<NotFoundException> {
            services.users.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->

        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com", "H42xS")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com", "H42xS")
        db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        assertEquals(listOf(user1, user2, user3), services.users.getAllUsers(0, 10).users)
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {

        assertEquals(emptyList(), services.users.getAllUsers(0, 10).users)
    }

    @Test
    fun `getAllUsers throws InvalidArgumentException if the skip is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.getAllUsers(-5, 10)
        }
    }

    @Test
    fun `getAllUsers throws InvalidArgumentException if the limit is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.getAllUsers(0, -5)
        }
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        val aid =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        val activities = services.users.getUserActivities(uid, 0, 10).activities

        assertEquals(
            listOf(Activity(aid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid, sid, 1)),
            activities
        )
    }

    @Test
    fun `getUserActivities throws InvalidArgumentException if the uid is not positive`() {
        assertFailsWith<InvalidArgumentException> {
            services.users.getUserActivities(-5, 0, 10).activities
        }
    }

    @Test
    fun `getUserActivities throws InvalidArgumentException if the skip is invalid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        assertFailsWith<InvalidArgumentException> {
            services.users.getUserActivities(uid, -4, 10).activities
        }
    }

    @Test
    fun `getUserActivities throws InvalidArgumentException if the limit is invalid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        assertFailsWith<InvalidArgumentException> {
            services.users.getUserActivities(uid, 0, -4).activities
        }
    }
}
