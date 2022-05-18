import apiFetch from "../../js/apiFetch.js";
import Route from "../../components/routes/Route.js";
import {LogError} from "../../js/errorUtils.js";
import {reloadHash} from "../../js/utils.js";

/**
 * @callback onUpdateCallback
 * @param {boolean} modified true if the element was modified, false otherwise
 */

/**
 * @typedef Route
 * @property {number} id - route id
 * @property {string} startLocation - route start location
 * @property {string} endLocation - route end location
 * @property {number} distance - route distance
 * @property {number} uid - route creator id
 */

/**
 * Route page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutePage(state) {

    const id = parseInt(state.params.id);
    if (isNaN(id))
        throw new LogError("Invalid param id");

    /**
     * @type {Route}
     */
    const route = await apiFetch(`/routes/${id}`);

    /**
     * Callback to be called when route is updated.
     * @param {boolean} modified true if route was modified, false otherwise
     */
    function onRouteUpdated(modified) {
        if (modified)
            reloadHash();
    }

    return Route(
        state,
        {
            ...route,
            onRouteUpdated
        }
    );
}

export default RoutePage;
