import {a, div, h3, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Activity details component.
 * @param state application state
 * @param props component properties
 * @returns activity component
 */
async function Activity(state, props) {
    let id = (state.params.id !== undefined) ? state.params.id : props.id;

    const activity = await apiFetch(`activities/${id}`)

    const sport = await apiFetch(`sports/${activity.sid}`)

    const route = activity.rid !== undefined ?
        await apiFetch(`routes/${activity.rid}`)
        : null;

    return div(
        div(
            div(
                h3(a({href: `#sports/${sport.id}`}, sport.name)),
                h3(activity.date),
                h3(activity.duration),
                route != null ?
                    h5(
                        a({href: `#routes/${route.id}`}, route.id.toString())
                    ) : undefined,
            )
        )
    );
}

export default Activity;
