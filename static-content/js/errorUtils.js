/**
 * Checks if the given object is an AppErrorPage.
 * @param error object to check
 * @returns true if the object is an AppErrorPage, false otherwise
 */
export function isAppError(error) {
    const errorLen = Object.keys(error).length;
    if (errorLen !== 3 && errorLen !== 4)
        return false;

    return error.code !== undefined &&
        error.name !== undefined &&
        error.description !== undefined &&
        (errorLen === 4 ? error.extraInfo !== undefined : true);
}

export function LogError(...data) {
    console.error(...data);
}

export function InvSearchParamsError(data) {
    console.error("Invalid URL search parameters", data);

    for (const prop in data)
        this[prop] = data[prop];
}
