package pt.isel.ls.sports.unit.services.sections.sports

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.InvalidArgumentException
import pt.isel.ls.sports.unit.services.AbstractServicesTests
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsServicesTests : AbstractServicesTests() {

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val sid = services.sports.createNewSport(token, "Powerlifting", "Get big")

        assertEquals(Sport(sid, "Powerlifting", uid, "Get big"), db.sports.getSport(conn, sid))
    }

    @Test
    fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val uid1 = services.sports.createNewSport(token, "Powerlifting", "Get big")
        val uid2 = services.sports.createNewSport(token, "Swimming", "Be like a fish")
        val uid3 = services.sports.createNewSport(token, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    @Test
    fun `createNewSport throws InvalidArgumentException if the name is invalid`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<InvalidArgumentException> {
            services.sports.createNewSport(token, "A", "Get big")
        }
    }

    @Test
    fun `createNewSport throws InvalidArgumentException if the description is invalid`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<InvalidArgumentException> {
            services.sports.createNewSport(token, "Powerlifting", "Lalalla".repeat(1000))
        }
    }

    @Test
    fun `createNewSport throws AuthenticationException if a user with the token doesn't exist`() {
        val token = "Lalala"

        assertFailsWith<AuthenticationException> {
            services.sports.createNewSport(token, "Powerlifting", "Get big")
        }
    }

    // getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        assertEquals(Sport(sid, "Soccer", uid, "Kick a ball to score a goal"), services.sports.getSport(sid))
    }

    @Test
    fun `getSport throws InvalidArgumentException if the sid is not positive`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getSport(-5)
        }
    }

    @Test
    fun `getSport throws NotFoundException if the sport with the sid doesn't exist`() {
        assertFailsWith<NotFoundException> {
            services.sports.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`(): Unit = db.execute { conn ->

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")
        db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")
        db.sports.createNewSport(conn, 1, "Basketball", "Shoot a ball through a hoop")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 1, "Get big")
        val sport3 = Sport(3, "Basketball", 1, "Shoot a ball through a hoop")

        assertEquals(listOf(sport1, sport2, sport3), services.sports.getAllSports(0, 10).sports)
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        assertEquals(emptyList(), services.sports.getAllSports(0, 10).sports)
    }

    @Test
    fun `getAllSports throws InvalidArgumentException if the skip is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getAllSports(-5, 10)
        }
    }

    @Test
    fun `getAllSports throws InvalidArgumentException if the limit is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getAllSports(0, -5)
        }
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        val activities = services.sports.getSportActivities(sid, 0, 10).activities

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, sid, 1)),
            activities
        )
    }

    @Test
    fun `getSportActivities throws InvalidArgumentException if the sid is not positive`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getSportActivities(-5, 0, 10)
        }
    }

    @Test
    fun `getSportActivities throws InvalidArgumentException if the skip is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getSportActivities(1, -5, 10)
        }
    }

    @Test
    fun `getSportActivities throws InvalidArgumentException if the limit is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.sports.getSportActivities(1, 0, -5)
        }
    }
}
