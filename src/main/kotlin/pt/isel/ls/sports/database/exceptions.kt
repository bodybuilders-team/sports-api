package pt.isel.ls.sports.database

class DatabaseAccessException : Exception()

class DatabaseRollbackException : Exception()

class NotFoundException(message: String? = null) : Exception(message)

class AlreadyExistsException(message: String? = null) : Exception(message)
