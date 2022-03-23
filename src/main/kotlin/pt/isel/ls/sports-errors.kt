package pt.isel.ls

import java.lang.Exception


class NotFoundException(override val message: String): Exception(message)
