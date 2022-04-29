import {h1} from "../../js/dom/domTags.js";

/**
 * Not Found page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function NotFoundPage(state) {
    return h1("Path " + state.path + " not found");
}

export default NotFoundPage;
