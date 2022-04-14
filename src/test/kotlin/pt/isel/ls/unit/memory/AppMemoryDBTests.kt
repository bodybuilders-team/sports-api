package pt.isel.ls.unit.memory

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

    // TODO: 26/03/2022 Add more tests (synchronize with AppPostgresTests?)
}
