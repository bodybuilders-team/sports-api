import {a, div, h1, h3, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropRoute
 * @property {string} id activity route id
 */

/**
 * Activity details component.
 *
 * @param state - application state
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {string} props.date - activity date
 * @param {string} props.duration - activity duration
 * @param {Object} props.sport - activity sport
 * @param {number} props.sport.id - activity sport id
 * @param {string} props.sport.name - activity sport name
 * @param {?PropRoute=} props.route - activity route
 *
 * @return Promise<HTMLElement>
 */
async function Activity(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Activity ${props.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Sport: ", a({href: `#sports/${props.sport.id}`}, props.sport.name)),
                h3("Date: ", props.date),
                h3("Duration: ", props.duration),
                props.route != null
                    ? h5("Route: ", a({href: `#routes/${props.route.id}`}, props.route.id.toString()))
                    : undefined,
            )
        )
    );
}

export default Activity;
