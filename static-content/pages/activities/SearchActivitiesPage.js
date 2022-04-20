import SearchActivitiesForm from "../../components/activities/SearchActivitiesForm.js";
import apiFetch from "../../js/apiFetch.js";
import Activities from "../../components/activities/Activities.js";
import {div} from "../../js/dom/domTags.js";

/**
 * Search activities page.
 * @param state application state
 * @returns search activities page
 */
async function SearchActivitiesPage(state) {

    const activitiesProps = getActivitiesProps();
    const activities = await getActivities(activitiesProps);

    function getActivitiesProps() {
        const activitiesProps = {};
        const searchParams = new URLSearchParams(window.location.search);
        activitiesProps.sid = searchParams.get("sid");
        activitiesProps.orderBy = searchParams.get("orderBy");
        activitiesProps.rid = searchParams.get("rid");
        activitiesProps.date = searchParams.get("date");

        return (activitiesProps.sid != null && activitiesProps.orderBy != null)
            ? activitiesProps
            : null;
    }

    function getActivities(activitiesProps) {
        if (activitiesProps == null)
            return null;

        const searchParams = new URLSearchParams();
        searchParams.set("sid", activitiesProps.sid);
        searchParams.set("orderBy", activitiesProps.orderBy);
        if (activitiesProps.rid != null) searchParams.set("rid", activitiesProps.rid);
        if (activitiesProps.date != null) searchParams.set("date", activitiesProps.date);

        return apiFetch("/activities?" + searchParams.toString())
            .then(json => json.activities);
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

        window.location.search = searchParams.toString();
    }

    return div(
        SearchActivitiesForm(state, {onSubmit: searchActivities, activitiesProps}),
        (activities != null)
            ? Activities(state, {activities})
            : undefined,
    );
}

export default SearchActivitiesPage;
