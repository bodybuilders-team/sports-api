import {br, div, h1, h3} from "../../js/dom/domTags.js";
import EditRoute from "./EditRoute.js";

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
 * @param {OnSubmitCallback} props.onUpdateSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function Route(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Route ${props.id}`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "routeStartLocation"}, "Start Location: ", props.startLocation),
                h3({id: "routeEndLocation"}, "End Location: ", props.endLocation),
                h3({id: "routeDistance"}, "Distance: ", props.distance.toString()),
                br(),
                EditRoute(state, props)
            )
        )
    );
}

export default Route;
