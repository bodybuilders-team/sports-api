import {div, h1} from "../../js/dom/domTags.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Activities from "../../components/activities/Activities.js";

/**
 * Activities page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function ActivitiesPage(state) {

    return div(
        h1({class: "app_icon"}, "Activities"),
        FetchedPaginatedCollection(state,
            {
                defaultSkip: 0,
                defaultLimit: 10,
                searchParams: {
                    sid: 1,
                    orderBy: "ascending",
                },
                collectionComponent: Activities,
                collectionEndpoint: "/activities",
                collectionName: "activities",
            }
        )
    )
}

export default ActivitiesPage;
