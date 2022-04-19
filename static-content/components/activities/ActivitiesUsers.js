import {a, div, h1} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";
import Sport from "../sports/Sport.js";

/**
 * Activities users activities component.
 * @param state application state
 * @returns activities users component
 */
async function ActivitiesUsers(state) {
    // TODO Ask professors if its necessary
    return div(
        h1("Activities " + state.params.id + " Users")
    );
}

export default ActivitiesUsers;
