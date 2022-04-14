import {div, h1} from "../../js/dom/domTags.js";

/**
 * Sport activities page.
 * @param state application state
 * @returns sport activities page
 */
function SportActivities(state) {
    return div(
        h1("Sport " + state.params.id + " Activities")
    );
}

export default SportActivities;
