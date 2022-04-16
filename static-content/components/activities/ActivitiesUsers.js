import {div, h1} from "../../js/dom/domTags.js";

/**
 * Activities users activities component.
 * @param state application state
 * @returns activities users component
 */
async function ActivitiesUsers(state) {
    return div(
        h1("Activities " + state.params.id + " Users")
    );
}

export default ActivitiesUsers;
