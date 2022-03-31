package pt.isel.ls.sports.data

import pt.isel.ls.sports.data.activities.ActivitiesDataMem
import pt.isel.ls.sports.data.routes.RoutesDataMem
import pt.isel.ls.sports.data.sports.SportsDataMem
import pt.isel.ls.sports.data.tokens.TokensDataMem
import pt.isel.ls.sports.data.users.UsersDataMem

class AppDataMem(source: AppDataMemSource) : AppDatabase {
    override val users = UsersDataMem(source)
    override val sports = SportsDataMem(source)
    override val routes = RoutesDataMem(source)
    override val tokens = TokensDataMem(source)
    override val activities = ActivitiesDataMem(source)
}
