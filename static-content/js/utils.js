import {InvalidSearchParamsError} from "./errorUtils.js";

/**
 * Creates an array with range from startNumber to endNumber (inclusive)
 *
 * @param {number} startNumber - Starting range number
 * @param {number} endNumber - Ending range number (inclusive)
 * @returns {number[]} array range
 */
export function range(startNumber, endNumber) {
    return Array(endNumber - startNumber + 1)
        .fill(0)
        .map((_, index) => startNumber + index);
}

/**
 * Gets skip and limit from query search parameters
 *
 * @param {Object} query - query object with search parameters
 * @param {number} defaultSkip - default skip if it does not exist in query
 * @param {number} defaultLimit - default limit if it does not exist in query
 * @returns {{limit: number, skip: number}}
 */
export function getQuerySkipLimit(query, defaultSkip, defaultLimit) {
    let {skip, limit} = query;

    skip = (skip != null) ? parseInt(skip) : defaultSkip;
    limit = (limit != null) ? parseInt(limit) : defaultLimit;

    if (skip == null || Number.isNaN(skip) || skip < 0)
        throw new InvalidSearchParamsError({error: `Skip must be a valid positive number, not ${query.skip}`})

    if (limit == null || Number.isNaN(limit) || limit < 0)
        throw new InvalidSearchParamsError({error: `Limit must be a valid positive number, not ${query.limit}`})

    return {skip, limit}
}