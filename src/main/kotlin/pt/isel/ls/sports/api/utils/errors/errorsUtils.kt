package pt.isel.ls.sports.api.utils.errors

import kotlinx.serialization.SerializationException
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.sports.api.exceptions.MissingTokenException
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.DatabaseAccessException
import pt.isel.ls.sports.database.exceptions.DatabaseRollbackException
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.services.exceptions.AuthenticationException
import pt.isel.ls.sports.services.exceptions.AuthorizationException
import pt.isel.ls.sports.utils.Logger

/**
 * Runs the given function [block] and returns the result as a Response.
 * Catches any exceptions and returns an error response accordingly.
 *
 * @param block function to execute
 * @return response from the executed [block] or error response, if any error occurs
 */
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
        ).toResponse(Status.INTERNAL_SERVER_ERROR)
    } catch (error: DatabaseRollbackException) {

        Logger.error(error.stackTraceToString())
        AppError(
            "INTERNAL_ERROR",
            "An internal error occurred",
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
    } catch (error: AuthorizationException) {

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
    } catch (error: MissingTokenException) {

        Logger.warn(error.toString())

        AppError(
            "MISSING_TOKEN",
            "Missing Authorization header token",
            error.message
        ).toResponse(Status.BAD_REQUEST)
    } catch (error: Exception) {
        Logger.error(error.stackTraceToString())

        AppError(
            "INTERNAL_ERROR",
            "An internal error occurred"
        ).toResponse(Status.INTERNAL_SERVER_ERROR)
    }
