package pt.isel.ls.unit.memory

import org.junit.Test
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsMemoryDBTests : AppMemoryDBTests() {
    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")

        assertEquals(Sport(rid, "Powerlifting", 1, "Get big"), source.sports[1])
    }

    @Test
    fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid1 = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")
        val uid2 = db.sports.createNewSport(conn, 1, "Swimming", "Be like a fish")
        val uid3 = db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sport = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.sports[1] = sport

        assertEquals(sport, db.sports.getSport(conn, 1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppException> {
                db.sports.getSport(conn, 1)
            }
        }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2, sport3), db.sports.getAllSports(conn))
    }

    @Test
    fun `getAllSports with no created sports returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.sports.getAllSports(conn))
    }
}
