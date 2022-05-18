import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * RouteCard component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - route id
 * @param {String} props.startLocation - route start location
 * @param {String} props.endLocation - route end location
 *
 * @returns {Promise<HTMLElement>}
 */
async function RouteCard(state, props) {

    const {id, startLocation, endLocation} = props;

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#routes/${id}`}, startLocation + " - " + endLocation)),
        )
    );
}

export default RouteCard;