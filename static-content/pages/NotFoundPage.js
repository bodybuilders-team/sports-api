import {h1} from "../js/dom/domTags.js";

/**
 * Not Found page.
 * @param state application state
 * @returns not found page
 */
async function NotFoundPage(state) {
    return h1("Path " + state.path + " not found");
}

export default NotFoundPage;