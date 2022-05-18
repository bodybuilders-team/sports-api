import {div, h1} from "../../js/dom/domTags.js";
import {validate} from "../../js/validationUtils.js";
import {InvalidSearchParamsError} from "../../js/errorUtils.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import UserCard from "../../components/users/UserCard.js";

/**
 * Users page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function UsersPage(state) {
    const usersProps = getUsersProps();

    //TODO: error handling for invalid activities props (show alertbox)

    /**
     * Parses activities props from state query params.
     *
     * @returns {?PropActivitiesProps}
     */
    function getUsersProps() {
        const usersProps = {};

        for (const key in state.query)
            usersProps[key] = state.query[key];

        if (state.query.sid != null)
            usersProps.sid = parseInt(state.query.sid);
        if (state.query.rid != null)
            usersProps.rid = parseInt(state.query.rid);

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

    return div(
        h1({class: "app-icon"}, "Users"),

        InfinitePaginatedCollection(state, {
            collectionComponent: UserCard,
            collectionEndpoint: "/activities/users",
            collectionName: "users",
            searchParams: usersProps
        })
    );
}

export default UsersPage;
