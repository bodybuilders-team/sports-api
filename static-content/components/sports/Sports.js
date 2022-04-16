import {a, div, h1} from "../../js/dom/domTags.js";
import Sport from "./Sport.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Sports page.
 * @param state application state
 * @returns sports page
 */
async function Sports(state) {
    const sports = await apiFetch(`sports`)
        .then(json => json.sports);


    return div(
        h1("Sports"),
        div(
            ...sports.map(sport =>
                div(
                    a({href: `#sports/${sport.id}`},
                        Sport(state, {sport}),
                    )
                )
            )
        )
    )
}

export default Sports;