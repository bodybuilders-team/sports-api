package pt.isel.ls.unit.services

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsServicesTests : AppServicesTests() {
    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`(): Unit = db.execute { conn ->

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

    // getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.execute { conn ->

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        assertEquals(Sport(1, "Soccer", 1, "Kick a ball to score a goal"), services.sports.getSport(1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {

        assertFailsWith<AppException> {
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

// getSportActivities

    @Test
    fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        val activities = services.sports.getSportActivities(1, 0, 10).activities

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }
}
