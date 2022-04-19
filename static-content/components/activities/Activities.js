import {a, div, h1, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Activities component.
 * @param state application state
 * @param props component properties
 * @returns activities component
 */
async function Activities(state, props) {
    let activities = (props != null) ? props.activities : undefined;

    if (activities == null)
        activities = await apiFetch(`activities?sid=1&orderBy=ascending`)
            .then(json => json.activities);

    for (const i in activities) {
        activities[i].sport = await apiFetch(`sports/${activities[i].sid}`)
            .then(sport => sport.name);
    }

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Activities"),
        ...activities.map(activity =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#activities/${activity.id}`}, `Activity ${activity.id}`))
                )
            )
        )
    );
}

export default Activities;