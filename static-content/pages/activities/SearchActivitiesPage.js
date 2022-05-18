import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import {div, h1} from "../../js/dom/domTags.js";
import Activities from "../../components/activities/Activities.js";

/**
 * @typedef ActivityData
 * @property {String} sid
 * @property {String} orderBy
 * @property {?String=} rid
 * @property {?String=} date
 */

/**
 * Search activities page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SearchActivitiesPage(state) {

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

    return div(
        h1({class: "app-icon"}, "Activities"),
        SearchActivitiesForm(
            state,
            {
                onSubmit: searchActivities
            }
        )
    );
}

export default SearchActivitiesPage;
