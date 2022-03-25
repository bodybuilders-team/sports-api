package pt.isel.ls.sports

fun String.substringOrNull(startIndex: Int): String? =
    if (this.length > startIndex) this.substring(startIndex) else null

fun String.toIntOrThrow(errorInfo: (() -> String)? = null): Int =
    this.toIntOrNull() ?: throw AppError.badRequest(errorInfo?.invoke() ?: "Error parsing $this to Int")
