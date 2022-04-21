import {div, h1} from "../../js/dom/domTags.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Activities from "../../components/activities/Activities.js";

/**
 * Activities page.
 * @param state application state
 * @returns activities page
 */
async function ActivitiesPage(state) {

    return div(
        h1({class: "app_icon"}, "Activities"),
        FetchedPaginatedCollection(state,
            {
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
