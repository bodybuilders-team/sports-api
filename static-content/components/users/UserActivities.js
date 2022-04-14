import {div, h1} from "../../js/dom/domTags.js";

/**
 * User activities page.
 * @param state application state
 * @returns user activities page
 */
function UserActivities(state) {
    return div(
        h1("User " + state.params.id + " Activities")
    );
}

export default UserActivities;
