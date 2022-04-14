import {API_BASE_URL} from "../../js/config.js";
import {a, div, h1, h5, h3} from "../../js/dom/domTags.js";

/**
 * Activities page.
 * @param state application state
 * @returns activities page
 */
async function Activities(state) {
    let activities = state.props.activities;

    if (activities == null) {
        activities = await fetch(API_BASE_URL + "/activities?sid=1&orderBy=ascending")
            .then(res => res.json())
            .then(json => json.activities);
    }

    for (const i in activities) {
        activities[i].sport = await fetch(API_BASE_URL + "/sports/" + activities[i].sid)
            .then(res => res.json())
            .then(sport => sport.name);
    }

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, "Activities"),
        ...activities.map(activity =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h3({class: "card-title"}, a({href: `#activities/${activity.id}`}, activity.id.toString())),
                    h5({class: "card-subtitle"}, a({href: `#sports/${activity.sid}`}, activity.sport))
                )
            )
        )
    );
}

export default Activities;