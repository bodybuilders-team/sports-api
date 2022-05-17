import {br, div, h1} from "../../js/dom/domTags.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import CreateRoute from "../../components/routes/CreateRoute.js";
import RouteCard from "../../components/routes/RouteCard.js";

/**
 * Routes page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutesPage(state) {

    function onRouteCreated(route) {
        window.location.hash = "#routes/" + route.id;
    }

    return div(
        h1({class: "app-icon"}, "Routes"),
        CreateRoute(state, {onRouteCreated}),
        br(),

        InfinitePaginatedCollection(state, {
            collectionComponent: RouteCard,
            collectionEndpoint: "/routes",
            collectionName: "routes"
        })
    )

}


export default RoutesPage;
