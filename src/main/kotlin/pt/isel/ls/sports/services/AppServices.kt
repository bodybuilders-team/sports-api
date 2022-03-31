package pt.isel.ls.sports.services

import pt.isel.ls.sports.data.AppDatabase

class AppServices(db: AppDatabase) {
    val users: UsersServices = UsersServices(db)
    val sports: SportsServices = SportsServices(db)
    val routes: RoutesServices = RoutesServices(db)
    val activities: ActivitiesServices = ActivitiesServices(db)
}
