import apiFetch from "../../js/apiFetch.js";
import Route from "../../components/routes/Route.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Route details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutePage(state) {
    if (state.params.id === undefined)
        throw new LogError("Route id must be defined")

    const id = state.params.id;
    const route = await apiFetch(`/routes/${id}`);

    return Route(
        state,
        {
            id: route.id,
            startLocation: route.startLocation,
            endLocation: route.endLocation,
            distance: route.distance
        }
    );
}

export default RoutePage;
