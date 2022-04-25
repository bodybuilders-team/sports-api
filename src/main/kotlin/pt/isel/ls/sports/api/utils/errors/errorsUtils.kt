package pt.isel.ls.sports.api.utils.errors

import kotlinx.serialization.SerializationException
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.sports.database.AlreadyExistsException
import pt.isel.ls.sports.database.DatabaseAccessException
import pt.isel.ls.sports.database.DatabaseRollbackException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.InvalidArgumentException
import pt.isel.ls.sports.services.UnauthorizedException
import pt.isel.ls.sports.utils.Logger

/**
 * Runs the given function [block] and returns the result as a Response.
 * Catches any exceptions and returns an error response accordingly.
 */
// TODO: 22/04/2022 - Place App errors in a separate file
inline fun runAndCatch(block: () -> Response): Response =
    try {
        block()
    } catch (error: SerializationException) {

        Logger.warn(error.toString())
        AppError(
            "BAD_REQUEST",
            "The request body is not valid"
        ).toResponse(Status.BAD_REQUEST)
    } catch (error: DatabaseAccessException) {

        Logger.error(error.stackTraceToString())
        AppError(
            "INTERNAL_ERROR",
            "An internal error occurred",
            error.message
        ).toResponse(Status.INTERNAL_SERVER_ERROR)
    } catch (error: DatabaseRollbackException) {

        Logger.error(error.stackTraceToString())
        AppError(
            "INTERNAL_ERROR",
            "An internal error occurred",
            error.message
        ).toResponse(Status.INTERNAL_SERVER_ERROR)
    } catch (error: NotFoundException) {

        Logger.warn(error.toString())
        AppError(
            "NOT_FOUND",
            "The requested resource was not found",
            error.message
        ).toResponse(Status.NOT_FOUND)
    } catch (error: AlreadyExistsException) {

        Logger.warn(error.toString())
        AppError(
            "ALREADY_EXISTS",
            "Resource already exists",
            error.message
        ).toResponse(Status.CONFLICT)
    } catch (error: InvalidArgumentException) {

        Logger.warn(error.toString())
        AppError(
            "BAD_REQUEST",
            "The request argument is not valid",
            error.message
        ).toResponse(Status.BAD_REQUEST)
    } catch (error: UnauthorizedException) {

        Logger.warn(error.toString())
        AppError(
            "UNAUTHORIZED",
            "You are not authorized to perform this action",
            error.message
        ).toResponse(Status.FORBIDDEN)
    } catch (error: AuthenticationException) {

        Logger.warn(error.toString())
        AppError(
            "UNAUTHENTICATED",
            "You are not authenticated",
            error.message
        ).toResponse(Status.UNAUTHORIZED)
    } catch (error: Exception) {
        Logger.error(error.stackTraceToString())

        AppError(
            "INTERNAL_ERROR",
            "An internal error occurred",
            error.message
        ).toResponse(Status.INTERNAL_SERVER_ERROR)
    }
