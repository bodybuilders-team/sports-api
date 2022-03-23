package pt.isel.ls.sports

import java.lang.Exception

// TODO: 23/03/2022 Comment
class NotFoundException(override val message: String) : Exception(message)
