import {div, h1, h3} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Route details component.
 * @param state application state
 * @param props component properties
 * @returns route component
 */
async function Route(state, props) {
    const route = (props != null && props.route != null)
        ? props.route
        : await apiFetch(`routes/${(state.params.id != null) ? state.params.id : props.id}`);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Route ${route.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Start Location: ", route.startLocation),
                h3("End Location: ", route.endLocation),
                h3("Distance: ", route.distance.toString())
            )
        )
    );
}

export default Route;
