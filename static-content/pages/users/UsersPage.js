import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Users from "../../components/users/Users.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Users page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function UsersPage(state) {
    return div(
        h1({class: "app_icon"}, "Users"),
        FetchedPaginatedCollection(state,
            {
                defaultSkip: 0,
                defaultLimit: 10,
                collectionComponent: Users,
                collectionEndpoint: "/users",
                collectionName: "users",
            }
        )
    )
}

export default UsersPage;
