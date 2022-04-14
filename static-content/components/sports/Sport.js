import {h1} from "../../js/dom/domTags.js";

/**
 * Sport details page.
 * @param state application state
 * @returns sport page
 */
function Sport(state) {
    return h1("Sport " + state.params.id);
}

export default Sport;
