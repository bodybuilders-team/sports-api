import {div, h1} from "../../js/dom/domTags.js";

/**
 * Activities users activities page.
 * @param state application state
 * @returns activities users page
 */
function ActivitiesUsers(state) {
    return div(
        h1("Activities " + state.params.id + " Users")
    );
}

export default ActivitiesUsers;
