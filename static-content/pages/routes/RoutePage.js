import apiFetch from "../../js/apiFetch.js";
import Route from "../../components/routes/Route.js";
import {LogError} from "../../js/errorUtils.js";
import {br, div} from "../../js/dom/domTags.js";
import {reloadHash} from "../../js/utils.js";

/**
 * Route details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RoutePage(state) {
    if (state.params.id === undefined)
        throw new LogError("Route id must be defined");

    const id = state.params.id;
    const route = await apiFetch(`/routes/${id}`);

    /**
     * Updates a route.
     * @param event form event
     */
    async function updateRoute(event) {
        event.preventDefault();
        const form = event.target;

        const startLocation = form.querySelector("#newRouteStartLocation").value;
        const endLocation = form.querySelector("#newRouteEndLocation").value;
        const distance = form.querySelector("#newRouteDistance").value; // TODO fix

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/routes/" + id,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance})
            }
        );

        const json = await res.json();

        if (res.ok) {
            reloadHash();
            return;
        }

        // TODO Improve alertBox
        const alertBox = form.parentNode.querySelector("#alert_box");
        alertBox
            ? alertBox.innerHTML = json.extraInfo
            : await form.parentNode.appendChild(
                await div(
                    br(),
                    div(
                        {id: "alert_box", class: "alert alert-warning", role: "alert"},
                        json.extraInfo
                    )
                )
            );
    }

    return Route(
        state,
        {
            id: route.id,
            startLocation: route.startLocation,
            endLocation: route.endLocation,
            distance: route.distance,
            onUpdateSubmit: updateRoute
        }
    );
}

export default RoutePage;
