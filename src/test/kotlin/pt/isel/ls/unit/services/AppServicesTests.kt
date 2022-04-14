package pt.isel.ls.unit.services

import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.services.AppServices
import kotlin.test.BeforeTest

abstract class AppServicesTests {

    private var source = AppMemoryDBSource()
    protected var db = AppMemoryDB(source)
    protected val services = AppServices(db)

    @BeforeTest
    fun initializeDataMem() {
        source = AppMemoryDBSource()
    }

    // TODO: 26/03/2022 Add more tests
}
