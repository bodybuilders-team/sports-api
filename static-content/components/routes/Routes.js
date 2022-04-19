import {a, div, h1, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Routes component.
 * @param state application state
 * @returns routes component
 */
async function Routes(state) {
    const routes = await apiFetch(`routes`)
        .then(json => json.routes);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Routes"),
        ...routes.map(route =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#routes/${route.id}`}, `Route ${route.id}`))
                )
            )
        )
    );
}

export default Routes;