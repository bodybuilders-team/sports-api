import {div} from "../../js/dom/domTags.js";
import RouteCard from "./RouteCard.js";

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
            RouteCard(state, route)
        )
    );
}

export default Routes;