import {br, div, h1} from "../../js/dom/domTags.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import CreateRoute from "../../components/routes/CreateRoute.js";
import RouteCard from "../../components/routes/RouteCard.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * Routes page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutesPage(state) {

    const storedUser = getStoredUser();

    /**
     * @typedef CreatedRoute
     * @property {number} id - route id.
     */

    /**
     * Callback for route creation.
     * @param {CreatedRoute} route - created route
     */
    function onRouteCreated(route) {
        window.location.hash = "#routes/" + route.id;
    }

    return div(
        h1({class: "app-icon"}, "Routes"),
        (storedUser != null)
            ? CreateRoute(state, {onRouteCreated})
            : undefined,
        br(),

        InfinitePaginatedCollection(state, {
            collectionComponent: RouteCard,
            collectionEndpoint: "/routes",
            collectionName: "routes"
        })
    );
}


export default RoutesPage;
