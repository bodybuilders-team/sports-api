import {a, div, h1} from "../../js/dom/domTags.js";
import Route from "./Route.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Routes component.
 * @param state application state
 * @returns routes component
 */
async function Routes(state) {
    const routes = await apiFetch(`routes`)
        .then(json => json.routes)

    return div(
        h1("Routes"),
        div(
            ...routes.map(route =>
                div(
                    a({href: `#routes/${route.id}`},
                        Route(state, {route}),
                    )
                )
            )
        )
    )
}

export default Routes;