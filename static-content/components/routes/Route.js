import {div, h1, h3} from "../../js/dom/domTags.js";

/**
 * Route details component.
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - route id
 * @param {string} props.startLocation route start location
 * @param {string} props.endLocation route end location
 * @param {number} props.distance route distance
 *
 * @return Promise<HTMLElement>
 */
async function Route(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Route ${props.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Start Location: ", props.startLocation),
                h3("End Location: ", props.endLocation),
                h3("Distance: ", props.distance.toString())
            )
        )
    );
}

export default Route;
