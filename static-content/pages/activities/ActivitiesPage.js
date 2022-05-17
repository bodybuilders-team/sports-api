import {br, div, h1} from "../../js/dom/domTags.js";
import {validate} from "../../js/validationUtils.js";
import {InvalidSearchParamsError} from "../../js/errorUtils.js";
import CreateActivity from "../../components/activities/CreateActivity.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import ActivityCard from "../../components/activities/ActivityCard.js";

async function ActivitiesPage(state) {
    const activitiesProps = getActivitiesProps();

    //TODO: error handling for invalid activities props (show alertbox)

    /**
     * Parses activities props from state query params.
     *
     * @returns {?PropActivitiesProps}
     */
    function getActivitiesProps() {
        const activitiesProps = {};

        for (const key in state.query)
            activitiesProps[key] = state.query[key];

        if (state.query.sid != null)
            activitiesProps.sid = parseInt(state.query.sid);
        if (state.query.rid != null)
            activitiesProps.rid = parseInt(state.query.rid);

        if (Object.keys(activitiesProps).length === 0)
            return null;

        const result = validate(activitiesProps, {
            sid: {type: "number", required: true},
            orderBy: {type: "string", required: true},
            rid: {type: "number"},
            date: {type: "string"}
        });

        if (!result.isValid)
            throw new InvalidSearchParamsError(result);

        return activitiesProps;
    }

    function onActivityCreated(activity) {
        window.location.hash = "#activities/" + activity.id;
    }

    return div(
        h1({class: "app-icon"}, "Activities"),
        CreateActivity(state, {onActivityCreated}),
        br(),

        InfinitePaginatedCollection(state, {
            collectionComponent: ActivityCard,
            collectionEndpoint: "/activities",
            collectionName: "activities",
            searchParams: activitiesProps
        })
    )
}


export default ActivitiesPage;