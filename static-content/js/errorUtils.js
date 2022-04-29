/**
 * Checks if the given object is an AppErrorPage.
 * @param {Object} error object to check
 * @returns {boolean} true if the object is an AppErrorPage, false otherwise
 */
export function isAppError(error) {
    const errorLen = Object.keys(error).length;
    if (errorLen !== 2 && errorLen !== 3)
        return false;

    return error.name !== undefined &&
        error.description !== undefined &&
        (errorLen === 3 ? error.extraInfo !== undefined : true);
}

/**
 * Error that prints itself (used to print stacktrace properly)
 *
 * @param data - error data
 * @constructor
 */
export function LogError(...data) {
    console.error(...data);
}

/**
 * Invalid search parameters error
 *
 * @param data
 * @constructor
 *
 * @property {Object} error - error data
 * @property {Object} details - error details
 */
export function InvalidSearchParamsError(data) {
    console.error("Invalid URL search parameters", data);

    this.error = data.error
    this.details = data.details
}
