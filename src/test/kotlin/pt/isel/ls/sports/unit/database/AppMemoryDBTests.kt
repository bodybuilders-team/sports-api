package pt.isel.ls.sports.unit.database

import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.AppMemoryDBSource
import kotlin.test.BeforeTest

abstract class AppMemoryDBTests {
    var source = AppMemoryDBSource()
    var db: AppMemoryDB = AppMemoryDB(source)

    @BeforeTest
    fun initializeDataMem() {
        source = AppMemoryDBSource()
        db = AppMemoryDB(source)
    }
}
