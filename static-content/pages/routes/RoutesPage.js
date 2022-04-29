import Routes from "../../components/routes/Routes.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Routes page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutesPage(state) {

    return div(
        h1({class: "app_icon"}, "Routes"),
        FetchedPaginatedCollection(state,
            {
                defaultSkip: 0,
                defaultLimit: 10,
                collectionComponent: Routes,
                collectionEndpoint: "/routes",
                collectionName: "routes",
            }
        )
    );
}


export default RoutesPage;
