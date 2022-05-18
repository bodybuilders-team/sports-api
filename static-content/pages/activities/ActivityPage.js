import apiFetch from "../../js/apiFetch.js";
import Activity from "../../components/activities/Activity.js";
import {LogError} from "../../js/errorUtils.js";
import {reloadHash} from "../../js/utils.js";

/**
 * Activity details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function ActivityPage(state) {
    if (state.params.id === undefined)
        throw new LogError("User id must be defined");

    const id = parseInt(state.params.id);
    if (id == null)
        throw new LogError("User id");

    const activity = await apiFetch(`/activities/${id}`);
    const user = await apiFetch(`/users/${activity.uid}`);
    const sport = await apiFetch(`/sports/${activity.sid}`);

    const route = activity.rid !== undefined
        ? await apiFetch(`/routes/${activity.rid}`)
        : null;

    /**
     * Callck to update the activity.
     * @param activity
     */
    function onActivityUpdated(activity) {
        reloadHash();
    }

    /**
     * Callback to delete the activity.
     */
    async function onActivityDeleted() {
        reloadHash();
    }

    return Activity(
        state,
        {
            id,
            date: activity.date,
            duration: activity.duration,
            user,
            sport,
            route,
            onActivityUpdated,
            onActivityDeleted
        }
    );
}

export default ActivityPage;
