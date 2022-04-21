import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import apiFetch from "../../js/apiFetch.js";
import Activities from "../../components/activities/Activities.js";
import {br, div} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "../../components/pagination/SkipPagination.js";

/**
 * Search activities page.
 * @param state application state
 * @returns search activities page
 */
async function SearchActivitiesPage(state) {
    const activitiesProps = getActivitiesProps()

    let {skip, limit, sid, orderBy, rid, date} = activitiesProps || {};

    const activitiesResponse = await getActivities(activitiesProps);

    skip = parseInt(skip) || 0;
    limit = parseInt(limit) || 10;


    function getActivitiesProps() {
        const activitiesProps = state.query || {}

        return (activitiesProps.sid != null && activitiesProps.orderBy != null)
            ? activitiesProps
            : null;
    }

    function getActivities(activitiesProps) {
        if (activitiesProps == null)
            return null

        const searchParams = new URLSearchParams();
        searchParams.set("sid", activitiesProps.sid);
        searchParams.set("orderBy", activitiesProps.orderBy);
        if (activitiesProps.rid != null) searchParams.set("rid", activitiesProps.rid);
        if (activitiesProps.date != null) searchParams.set("date", activitiesProps.date);
        if (activitiesProps.skip != null) searchParams.set("skip", activitiesProps.skip);
        if (activitiesProps.limit != null) searchParams.set("limit", activitiesProps.limit);

        return apiFetch("/activities?" + searchParams.toString())
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

    function onPageChange(page) {
        const skip = (page - 1) * limit;

        const searchParams = new URLSearchParams();
        searchParams.set("sid", sid);
        searchParams.set("orderBy", orderBy);
        if (rid != null) searchParams.set("rid", rid);
        if (date != null) searchParams.set("date", date);
        searchParams.set("skip", skip);
        searchParams.set("limit", limit);

        window.location.href = "#activities?" + searchParams.toString();
    }

    return div(
        SearchActivitiesForm(state, {onSubmit: searchActivities, activitiesProps}),
        br(),
        (activitiesResponse != null && activitiesResponse.activities.length > 0)
            ?
            div(
                Activities(state, {activities: activitiesResponse.activities}),
                SkipLimitPaginate(state, {
                    skip,
                    limit,
                    totalCount: activitiesResponse.totalCount,
                    onPageChange
                }),
            ) : undefined,
    );

}

export default SearchActivitiesPage;
