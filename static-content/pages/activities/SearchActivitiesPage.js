import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import {br, div} from "../../js/dom/domTags.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Activities from "../../components/activities/Activities.js";

/**
 * Search activities page.
 * @param state application state
 * @returns search activities page
 */
async function SearchActivitiesPage(state) {
    const activitiesProps = getActivitiesProps()

    let {sid, orderBy, rid, date} = activitiesProps || {};

    function getActivitiesProps() {
        const activitiesProps = state.query || {}

        return (activitiesProps.sid != null && activitiesProps.orderBy != null)
            ? activitiesProps
            : null;
    }

    /**
     * Search for activities form function.
     * @param event form event
     */
    function searchActivities(event) {
        event.preventDefault();
        const form = event.target;

        const sid = form.querySelector("#sid").value;
        const orderBy = form.querySelector("#orderBy").value;
        const rid = form.querySelector("#rid").value;
        const date = form.querySelector("#date").value;

        const searchParams = new URLSearchParams();
        searchParams.set("sid", sid);
        searchParams.set("orderBy", orderBy);
        if (rid !== "") searchParams.set("rid", rid);
        if (date !== "") searchParams.set("date", date);

        window.location.href = "#activities?" + searchParams.toString();
    }


    return div(
        SearchActivitiesForm(state, {onSubmit: searchActivities, activitiesProps}),
        br(),
        (activitiesProps != null) ?
            FetchedPaginatedCollection(state,
                {
                    initialSkip: 0,
                    initialLimit: 10,
                    searchParams: {
                        sid,
                        orderBy,
                        rid,
                        date,
                    },
                    collectionComponent: Activities,
                    collectionEndpoint: "/activities",
                    collectionName: "activities",
                }
            )
            : undefined
    )


}

export default SearchActivitiesPage;
