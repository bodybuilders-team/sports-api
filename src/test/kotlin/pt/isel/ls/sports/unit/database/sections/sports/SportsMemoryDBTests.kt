package pt.isel.ls.sports.unit.database.sections.sports

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.database.AppMemoryDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SportsMemoryDBTests : AppMemoryDBTests(), SportsDBTests {

    // createNewSport

    @Test
    override fun `createNewSport creates sport correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")

        assertEquals(Sport(rid, "Powerlifting", 1, "Get big"), source.sports[1])
    }

    @Test
    override fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid1 = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")
        val uid2 = db.sports.createNewSport(conn, 1, "Swimming", "Be like a fish")
        val uid3 = db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    @Test
    override fun `createNewSport throws NotFoundException if there's no user with the uid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.sports.createNewSport(conn, 99999, "Powerlifting", "Get big")
            }
        }

    // getSport

    @Test
    override fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sport = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.sports[1] = sport

        assertEquals(sport, db.sports.getSport(conn, 1))
    }

    @Test
    override fun `getSport throws NotFoundException if the sport with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.sports.getSport(conn, 1)
            }
        }

    // getAllSports

    @Test
    override fun `getAllSports returns list of all sport objects`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2, sport3), db.sports.getAllSports(conn, 0, 10).sports)
    }

    @Test
    override fun `getAllSports with no created sports returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.sports.getAllSports(conn, 0, 10).sports)
    }

    @Test
    override fun `getAllSports with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport2, sport3), db.sports.getAllSports(conn, 1, 10).sports)
    }

    @Test
    override fun `getAllSports with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2), db.sports.getAllSports(conn, 0, 2).sports)
    }

    // hasSport

    @Test
    override fun `hasSport returns true if the sport exists`(): Unit = db.execute { conn ->
        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.sports[1] = sport1

        assertTrue(db.sports.hasSport(conn, 1))
    }

    @Test
    override fun `hasSport returns false if the sport does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.sports.hasSport(conn, 1))
    }
}
