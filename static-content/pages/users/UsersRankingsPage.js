import {br, div, h1, h2} from "../../js/dom/domTags.js";
import {validate} from "../../js/validationUtils.js";
import {InvalidSearchParamsError} from "../../js/errorUtils.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import UsersRankingCard from "../../components/users/UsersRankingCard.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Users page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function UsersRankingsPage(state) {

    const activityProps = getActivityProps();

    /**
     * Parses activitity props from state query params.
     *
     * @returns {?PropActivitiesProps}
     */
    function getActivityProps() {
        const usersProps = {};

        for (const key in state.query)
            usersProps[key] = state.query[key];

        if (state.query.sid != null) {
            usersProps.sid = parseInt(state.query.sid);

            if (isNaN(usersProps.sid))
                throw new InvalidSearchParamsError({error: "Invalid sid"});
        }

        if (state.query.rid != null) {
            usersProps.rid = parseInt(state.query.rid);

            if (isNaN(usersProps.rid))
                throw new InvalidSearchParamsError({error: "Invalid rid"});
        }

        if (Object.keys(usersProps).length === 0)
            return null;

        const result = validate(usersProps, {
            sid: {type: "number", required: true},
            rid: {type: "number"},
        });

        if (!result.isValid)
            throw new InvalidSearchParamsError(result);

        return usersProps;
    }

    const sport = await apiFetch(`/sports/${activityProps.sid}`)
    const route = (activityProps.rid != null) ? await apiFetch(`/routes/${activityProps.rid}`) : null

    return div(
        h1({class: "app-icon"}, "Users Rankings"),
        br(),
        h2(`Sport: ${sport.name}`),
        (route != null)
            ? h2(`Route: ${route.startLocation + "-" + route.endLocation}`)
            :
            null,
        InfinitePaginatedCollection(state, {
            collectionComponent: UsersRankingCard,
            collectionEndpoint: "/activities/users",
            collectionName: "activitiesUsers",
            searchParams: activityProps
        })
    );
}

export default UsersRankingsPage;
