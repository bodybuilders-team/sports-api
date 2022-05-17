package pt.isel.ls.sports.unit.database.sections.sports

import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
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
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val rid = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")

        assertEquals(Sport(rid, "Powerlifting", 1, "Get big"), source.sports[1])
    }

    @Test
    override fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val uid1 = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")
        val uid2 = db.sports.createNewSport(conn, 1, "Swimming", "Be like a fish")
        val uid3 = db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // updateSport

    @Test
    override fun `updateSport updates a sport correctly`(): Unit = db.execute { conn ->
        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.sports[1] = sport1

        val newName = "new name"
        val newDescription = "new desc"
        db.sports.updateSport(conn, 1, newName, newDescription)

        val updatedSport = db.sports.getSport(conn, 1)

        assertEquals(newName, updatedSport.name)
        assertEquals(newDescription, updatedSport.description)
    }

    @Test
    override fun `updateSport returns true if a sport was modified`(): Unit = db.execute { conn ->
        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.sports[1] = sport1

        val newName = "new name"
        val newDescription = "new desc"
        assertTrue(db.sports.updateSport(conn, 1, newName, newDescription))
    }

    @Test
    override fun `updateSport returns false if a sport was not modified`(): Unit = db.execute { conn ->
        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.sports[1] = sport1

        assertFalse(db.sports.updateSport(conn, 1, sport1.name, sport1.description))
    }

    @Test
    override fun `updateSport throws NotFoundException if there's no sport with the sid`(): Unit = db.execute { conn ->
        assertFailsWith<NotFoundException> {
            db.sports.updateSport(conn, 9999, "new name", "new desc")
        }
    }

    @Test
    override fun `throws InvalidArgumentException if name and description are both null`(): Unit = db.execute { conn ->
        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.sports[1] = sport1

        assertFailsWith<InvalidArgumentException> {
            db.sports.updateSport(conn, 1, null, null)
        }
    }

    // getSport

    @Test
    override fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

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

    // searchSports

    @Test
    override fun `searchSports returns list of all sport objects`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2, sport3), db.sports.searchSports(conn, 0, 10).sports)
    }

    @Test
    override fun `searchSports with no created sports returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.sports.searchSports(conn, 0, 10).sports)
    }

    @Test
    override fun `searchSports with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport2, sport3), db.sports.searchSports(conn, 1, 10).sports)
    }

    @Test
    override fun `searchSports with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2), db.sports.searchSports(conn, 0, 2).sports)
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
