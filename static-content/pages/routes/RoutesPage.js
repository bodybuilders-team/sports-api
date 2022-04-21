import Routes from "../../components/routes/Routes.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Routes page.
 * @param state application state
 * @returns routes page
 */
async function RoutesPage(state) {

    return div(
        h1({class: "app_icon"}, "Routes"),
        FetchedPaginatedCollection(state,
            {
                initialSkip: 0,
                initialLimit: 10,
                collectionComponent: Routes,
                collectionEndpoint: "/routes",
                collectionName: "routes",
            }
        )
    )
}


export default RoutesPage;
