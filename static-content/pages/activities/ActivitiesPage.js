import {br, div, h1} from "../../js/dom/domTags.js";
import {validate} from "../../js/validationUtils.js";
import {InvalidSearchParamsError} from "../../js/errorUtils.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import ActivityCard from "../../components/activities/ActivityCard.js";

/**
 * Activities page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function ActivitiesPage(state) {

    const activitiesProps = getActivitiesProps();

    /**
     * Parses activities props from state query params.
     *
     * @returns {?PropActivitiesProps}
     */
    function getActivitiesProps() {
        const activitiesProps = {};

        for (const key in state.query)
            activitiesProps[key] = state.query[key];

        if (state.query.sid != null) {
            activitiesProps.sid = parseInt(state.query.sid);

            if (isNaN(activitiesProps.sid))
                throw new InvalidSearchParamsError({error: "Invalid sid"});
        }
        if (state.query.rid != null) {
            activitiesProps.rid = parseInt(state.query.rid);

            if (isNaN(activitiesProps.rid))
                throw new InvalidSearchParamsError({error: "Invalid rid"});
        }

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


    return div(
        h1({class: "app-icon"}, "Activities"),
        br(),

        InfinitePaginatedCollection(state, {
            collectionComponent: ActivityCard,
            collectionEndpoint: "/activities",
            collectionName: "activities",
            searchParams: activitiesProps
        })
    );
}


export default ActivitiesPage;
