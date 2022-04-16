import {a, div, h3} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Activities component.
 * @param state application state
 * @param props component properties
 * @returns activities component
 */
async function Activities(state, props) {
    let activities = (props != null) ? props.activities : undefined;

    if (activities == null) {
        activities = await apiFetch(`activities?sid=1&orderBy=ascending`)
            .then(json => json.activities);
    }

    for (const i in activities) {
        activities[i].sport = await apiFetch(`sports/${activities[i].sid}`)
            .then(sport => sport.name);
    }

    return div(
        ...activities.map(activity =>
            div(
                a(
                    {href: `#activities/${activity.id}`},
                    h3(activity.sport),
                    h3(activity.date),
                    h3(activity.duration)
                )
            )
        )
    );
}

export default Activities;