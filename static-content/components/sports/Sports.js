import {a, div, h1, h5} from "../../js/dom/domTags.js";
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
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Sports"),
        ...sports.map(sport =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#sports/${sport.id}`}, sport.name))
                )
            )
        )
    );
}

export default Sports;