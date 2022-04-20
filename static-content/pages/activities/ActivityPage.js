import apiFetch from "../../js/apiFetch.js";
import Activity from "../../components/activities/Activity.js";

/**
 * Activity details page.
 * @param state application state
 * @returns Activity page
 */
async function ActivityPage(state) {
    if (state.params.id === undefined)
        throw new Error("User id must be defined")

    const id = state.params.id;
    const activity = await apiFetch(`/activities/${id}`);

    const sport = await apiFetch(`/sports/${activity.sid}`);

    const route = activity.rid !== undefined
        ? await apiFetch(`/routes/${activity.rid}`)
        : null;

    return Activity(
        state,
        {
            id,
            date: activity.date,
            duration: activity.duration,
            sport,
            route
        }
    );
}

export default ActivityPage;
