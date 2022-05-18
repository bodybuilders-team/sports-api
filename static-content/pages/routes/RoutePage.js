import apiFetch from "../../js/apiFetch.js";
import Route from "../../components/routes/Route.js";
import {LogError} from "../../js/errorUtils.js";
import {reloadHash} from "../../js/utils.js";

/**
 * Route details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutePage(state) {
    if (state.params.id === undefined)
        throw new LogError("Route id must be defined");

    const id = state.params.id;
    const route = await apiFetch(`/routes/${id}`);

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
