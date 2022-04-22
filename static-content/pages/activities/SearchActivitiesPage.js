import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import {br, div} from "../../js/dom/domTags.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Activities from "../../components/activities/Activities.js";
import {InvSearchParamsError} from "../../js/errorUtils.js";
import {validate} from "../../js/validationUtils.js";

/**
 * Search activities page.
 * @param state application state
 * @returns search activities page
 */
async function SearchActivitiesPage(state) {

    function getActivitiesProps() {
        if (Object.keys(state.query).length === 0)
            return null

        const result = validate(state.query, {
            sid: {type: "string", required: true},
            orderBy: {type: "string", required: true},
            rid: {type: "string"},
            date: {type: "string"},
            skip: {type: "string"},
            limit: {type: "string"},
        })

        if (!result.isValid)
            throw new InvSearchParamsError(result);

        return state.query
    }

    const activitiesProps = getActivitiesProps()

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
        SearchActivitiesForm(state, {onSubmit: searchActivities, activitiesProps: activitiesProps}),
        br(),
        (activitiesProps != null) ?
            FetchedPaginatedCollection(state,
                {
                    initialSkip: 0,
                    initialLimit: 10,
                    searchParams: activitiesProps,
                    collectionComponent: Activities,
                    collectionEndpoint: "/activities",
                    collectionName: "activities",
                }
            )
            : undefined
    )


}

export default SearchActivitiesPage;
