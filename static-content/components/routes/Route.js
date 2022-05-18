import {br, div, h1, h3} from "../../js/dom/domTags.js";
import EditRoute from "./EditRoute.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * Route component.
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - route id
 * @param {string} props.startLocation route start location
 * @param {string} props.endLocation route end location
 * @param {number} props.distance route distance
 * @param {number} props.uid - route creator id
 * @param {onUpdateCallback} props.onRouteUpdated - callback to be called when route is updated
 *
 * @return Promise<HTMLElement>
 */
async function Route(state, props) {

    const {id, startLocation, endLocation, distance, uid, onRouteUpdated} = props;
    const storedUser = getStoredUser();

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Route`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "routeStartLocation"}, "Start Location: ", startLocation),
                h3({id: "routeEndLocation"}, "End Location: ", endLocation),
                h3({id: "routeDistance"}, "Distance: ", distance.toString(), " Km"),
                br(),
                (storedUser != null && storedUser.uid === uid)
                    ? EditRoute(state, {id, onRouteUpdated})
                    : undefined
            )
        )
    );
}

export default Route;
