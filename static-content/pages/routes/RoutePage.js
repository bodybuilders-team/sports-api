import apiFetch from "../../js/apiFetch.js";
import Route from "../../components/routes/Route.js";

/**
 * Route details page.
 * @param state application state
 * @returns route page
 */
async function RoutePage(state) {
    if (state.params.id === undefined)
        throw new Error("Route id must be defined")

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
