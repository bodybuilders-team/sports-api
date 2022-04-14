import {API_BASE_URL} from "../../js/config.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Sports page.
 * @param state application state
 * @returns sports page
 */
async function Sports(state) {
    return fetch(API_BASE_URL + "/sports")
        .then(res => res.json())
        .then(json => json.sports)
        .then(sports => {
            div(
                h1("Sports")
            )
        });
}

export default Sports;