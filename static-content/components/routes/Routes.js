import {API_BASE_URL} from "../../js/config.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Routes page.
 * @param state application state
 * @returns routes page
 */
function Routes(state) {
    return fetch(API_BASE_URL + "routes")
        .then(res => res.json())
        .then(json => json.routes)
        .then(routes => {
            div(
                h1("Routes")
            )
        });
}

export default Routes;