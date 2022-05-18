import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import {br, div, h1} from "../../js/dom/domTags.js";
import Activities from "../../components/activities/Activities.js";
import CreateActivity from "../../components/activities/CreateActivity.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * @typedef ActivityData
 * @property {String} sid
 * @property {String} orderBy
 * @property {?String=} rid
 * @property {?String=} date
 */

/**
 * Search activities page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SearchActivitiesPage(state) {

    const storedUser = getStoredUser();

    /**
     * Search for activities form function.
     * @param {ActivityData} activityData - activity data
     */
    function searchActivities(activityData) {
        const {sid, orderBy, rid, date} = activityData;

        const searchParams = new URLSearchParams();
        searchParams.set("sid", sid);
        searchParams.set("orderBy", orderBy);
        if (rid != null) searchParams.set("rid", rid);
        if (date != null) searchParams.set("date", date);

        window.location.href = "#activities?" + searchParams.toString();
    }

    /**
     * @typedef Activity
     * @property {number} id - activity id
     */

    /**
     * Callback for when an activity is created.
     * @param {Activity} activity - created activity
     */
    function onActivityCreated(activity) {
        window.location.hash = "#activities/" + activity.id;
    }

    return div(
        h1({class: "app-icon"}, "Activities"),
        (storedUser != null)
            ? CreateActivity(state, {onActivityCreated})
            : undefined,
        br(),

        SearchActivitiesForm(
            state,
            {
                onSubmit: searchActivities
            }
        )
    );
}

export default SearchActivitiesPage;
