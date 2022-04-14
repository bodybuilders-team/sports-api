import {h1} from "../../js/dom/domTags.js";

/**
 * Route details page.
 * @param state application state
 * @returns route page
 */
function Route(state) {
    return h1("Route " + state.params.id);
}

export default Route;
