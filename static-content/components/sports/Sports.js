import {a, div, h1, h5} from "../../js/dom/domTags.js";

/**
 * Sports page.
 * @param state application state
 * @param props component properties
 * @returns sports page
 */
async function Sports(state, props) {
    if (props == null)
        throw new Error("Sports props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Sports"),
        ...props.sports.map(sport =>
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