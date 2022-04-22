package pt.isel.ls.sports.services

class AuthenticationException(message: String? = null) : Exception(message)

class InvalidArgumentException(message: String? = null) : Exception(message)

class UnauthorizedException(message: String? = null) : Exception(message)
