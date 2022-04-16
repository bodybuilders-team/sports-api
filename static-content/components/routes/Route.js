import {div, h3} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Route details component.
 * @param state application state
 * @returns route component
 */
async function Route(state, props) {
    const route = (props != null && props.route != null) ? props.route
        : await apiFetch(`routes/${(state.params.id != null) ? state.params.id : props.id}`);

    return div(
        h3(route.startLocation),
        h3(route.endLocation),
        h3(route.distance.toString())
    );
}

export default Route;
