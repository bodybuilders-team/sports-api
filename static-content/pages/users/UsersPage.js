import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Users from "../../components/users/Users.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function UsersPage(state) {
    return div(
        h1({class: "app_icon"}, "Users"),
        FetchedPaginatedCollection(state,
            {
                initialSkip: 0,
                initialLimit: 10,
                collectionComponent: Users,
                collectionEndpoint: "/users",
                collectionName: "users",
            }
        )
    )
}

export default UsersPage;
