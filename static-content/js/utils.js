import {InvalidSearchParamsError} from "./errorUtils.js";
import AlertBox from "../components/AlertBox.js";

/**
 * Creates an array with range from startNumber to endNumber (inclusive).
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
 * Gets skip and limit from query search parameters.
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
        throw new InvalidSearchParamsError({error: `Skip must be a valid positive number, not ${query.skip}`});

    if (limit == null || Number.isNaN(limit) || limit < 0)
        throw new InvalidSearchParamsError({error: `Limit must be a valid positive number, not ${query.limit}`});

    return {skip, limit};
}

/**
 * Dispatches an HashChangeEvent to the window.
 */
export function reloadHash() {
    window.dispatchEvent(new HashChangeEvent("hashchange"));
}

export function changeLocation(location) {
    if (location == null)
        window.dispatchEvent(new HashChangeEvent("hashchange"));

    const formatedLocation = location.substring(1);
    const currentHash = window.location.hash.replace("#", "");

    if (currentHash !== formatedLocation)
        window.location.hash = formatedLocation;
    else
        window.dispatchEvent(new HashChangeEvent("hashchange"));
}

/**
 * Creates a new AlertBox with the given error in the form element.
 *
 * @param {Object} state - application state
 * @param {HTMLElement} element - element
 * @param errorMessage - error message
 *
 * @returns Promise<HTMLElement>
 */
export async function alertBoxWithError(state, element, errorMessage) {
    const alertBox = element.querySelector("#alert_box");
    alertBox
        ? alertBox.textContent = errorMessage
        : await element.appendChild(await AlertBox(state, {
            alertLevel: "warning",
            alertMessage: errorMessage
        }));
}

/**
 * @typedef {Promise} Ref
 * @property {Function} resolve - resolves the promise
 * @property {Function} reject - rejects the promise
 */

/**
 * Creates a reference using a deferred promise.
 * @returns {Ref}
 */
export function createRef() {
    let resolve, reject;

    let promise = new Promise((_resolve, _reject) => {
        resolve = _resolve;
        reject = _reject;
    })

    promise.resolve = resolve;
    promise.reject = reject;

    return promise;
}

/**
 * @typedef StoredUser
 * @property {number} uid - user unique identifier
 * @property {string} token - user token
 */

/**
 * Gets the user stored in the local storage, if exists.
 * @returns {?StoredUser} stored user, or null if not exists
 */
export function getStoredUser() {
    const user = localStorage.getItem("user");

    if (user == null)
        return null;

    return JSON.parse(user);
}

/**
 * Stores the user in the local storage.
 * @param {StoredUser} user - user to be stored
 */
export function storeUser(user) {
    localStorage.setItem("user", JSON.stringify(user));
}