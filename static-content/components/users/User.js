import {h1} from "../../js/dom/domTags.js";

/**
 * User details page.
 * @param state application state
 * @returns user page
 */
function User(state) {
    return h1("User " + state.params.id);
}

export default User;
