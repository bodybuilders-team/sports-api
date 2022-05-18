import apiFetch from "../../js/apiFetch.js";
import Activity from "../../components/activities/Activity.js";
import {LogError} from "../../js/errorUtils.js";
import {reloadHash} from "../../js/utils.js";

/**
 * @callback onDeleteCallback
 */

/**
 * Activity details page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function ActivityPage(state) {

    const id = parseInt(state.params.id);
    if (isNaN(id))
        throw new LogError("Invalid param id");

    const activity = await apiFetch(`/activities/${id}`);
    const user = await apiFetch(`/users/${activity.uid}`);
    const sport = await apiFetch(`/sports/${activity.sid}`);

    const route = activity.rid !== undefined
        ? await apiFetch(`/routes/${activity.rid}`)
        : null;

    /**
     * Callback to be called when activity is updated.
     * @param {boolean} modified true if activity was modified, false otherwise
     */
    function onActivityUpdated(modified) {
        if (modified)
            reloadHash();
    }

    /**
     * Callback to be called when activity is deleted.
     */
    async function onActivityDeleted() {
        window.location.hash = "#activities/search";
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
