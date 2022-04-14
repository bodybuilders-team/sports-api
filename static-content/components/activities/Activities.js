import {API_BASE_URL} from "../../js/config.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Activities page.
 * @param state application state
 * @returns activities page
 */
async function Activities(state) {
    return fetch(API_BASE_URL + "activities")
        .then(res => res.json())
        .then(json => json.activities)
        .then(activities => {
            div(
                h1("Activities")
            )
        });
}

export default Activities;