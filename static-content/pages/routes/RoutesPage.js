import Routes from "../../components/routes/Routes.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {br, div, h1} from "../../js/dom/domTags.js";
import CreateRoute from "../../components/routes/CreateRoute.js";
import {alertBoxWithError, reloadHash} from "../../js/utils.js";

/**
 * Routes page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutesPage(state) {

    /**
     * Creates a route.
     * @param event form event
     */
    async function createRoute(event) {
        event.preventDefault();
        const form = event.target;

        const startLocation = form.querySelector("#routeStartLocation").value;
        const endLocation = form.querySelector("#routeEndLocation").value;
        const distance = form.querySelector("#routeDistance").value;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/routes/",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance: Number(distance)})
            }
        );

        const json = await res.json();

        if (res.ok)
            reloadHash()
        else
            await alertBoxWithError(state, form, json);
    }

    return div(
        h1({class: "app-icon"}, "Routes"),
        CreateRoute(state, {onCreateSubmint: createRoute}),
        br(),
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
