import apiFetch from "../../js/apiFetch.js";
import Activities from "../../components/activities/Activities.js";

/**
 * Activities page.
 * @param state application state
 * @returns activities page
 */
async function ActivitiesPage(state) {

    const activities = await apiFetch(`activities?sid=1&orderBy=ascending`)
        .then(json => json.activities);

    return Activities(state, {activities});
}

export default ActivitiesPage;
