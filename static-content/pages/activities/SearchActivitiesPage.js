import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import {br, div} from "../../js/dom/domTags.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Activities from "../../components/activities/Activities.js";
import {InvalidSearchParamsError} from "../../js/errorUtils.js";
import {validate} from "../../js/validationUtils.js";

/**
 * Search activities page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SearchActivitiesPage(state) {

    /**
     * Parses activities props from state query params.
     *
     * @returns {?PropActivitiesProps}
     */
    function getActivitiesProps() {
        const activitiesProps = {}

        for (const key in state.query)
            activitiesProps[key] = state.query[key]

        if (state.query.sid != null)
            activitiesProps.sid = parseInt(state.query.sid)
        if (state.query.rid != null)
            activitiesProps.rid = parseInt(state.query.rid)
        if (state.query.skip != null)
            activitiesProps.skip = parseInt(state.query.skip)
        if (state.query.limit != null)
            activitiesProps.limit = parseInt(state.query.limit)

        if (Object.keys(activitiesProps).length === 0)
            return null

        const result = validate(activitiesProps, {
            sid: {type: "number", required: true},
            orderBy: {type: "string", required: true},
            rid: {type: "number"},
            date: {type: "string"},
            skip: {type: "number"},
            limit: {type: "number"},
        })

        if (!result.isValid)
            throw new InvalidSearchParamsError(result);

        return activitiesProps
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
                    defaultSkip: 0,
                    defaultLimit: 10,
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
