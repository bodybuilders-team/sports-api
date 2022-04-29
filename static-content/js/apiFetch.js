import {API_BASE_URL} from "./config.js";
import {isAppError, LogError} from "./errorUtils.js";

/**
 * Executes a fetch to the API.
 *
 * @param {string} endpoint - fetch endpoint
 * @returns {any} response in json
 */
async function apiFetch(endpoint) {
    let json
    try {
        const res = await fetch(`${API_BASE_URL}${endpoint}`);
        json = await res.json();

        if (res.ok)
            return json;

    } catch (err) {
        if (isAppError(err))
            throw err;

        throw new LogError(err);
    }

    throw new LogError(json)
}

export default apiFetch;