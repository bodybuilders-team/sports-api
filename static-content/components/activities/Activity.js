import {a, div, h1, h3, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Activity details component.
 * @param state application state
 * @param props component properties
 * @returns activity component
 */
async function Activity(state, props) {
    const id = (state.params.id !== undefined) ? state.params.id : props.id;
    const activity = await apiFetch(`activities/${id}`);

    const sport = await apiFetch(`sports/${activity.sid}`);

    const route = activity.rid !== undefined
        ? await apiFetch(`routes/${activity.rid}`)
        : null;

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Activity ${activity.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Sport: ", a({href: `#sports/${sport.id}`}, sport.name)),
                h3("Date: ", activity.date),
                h3("Duration: ", activity.duration),
                route != null
                    ? h5("Route: ", a({href: `#routes/${route.id}`}, route.id.toString()))
                    : undefined,
            )
        )
    );
}

export default Activity;
