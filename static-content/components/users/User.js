import {h1} from "../../js/dom/domTags.js";

/**
 * User details page.
 * @param state application state
 * @returns user page
 */
async function User(state) {
    return h1("User " + state.params.id);
}

export default User;
