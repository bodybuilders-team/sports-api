import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropRoute
 * @property {number} id route id
 */

/**
 * Routes component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {PropRoute[]} props.routes - routes Ids
 *
 * @return Promise<HTMLElement>
 */
async function Routes(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        ...props.routes.map(route =>
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