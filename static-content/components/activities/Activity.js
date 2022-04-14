import {h1} from "../../js/dom/domTags.js";

/**
 * Activity details page.
 * @param state application state
 * @returns activity page
 */
async function Activity(state) {
    return h1("Activity " + state.params.id);
}

export default Activity;
