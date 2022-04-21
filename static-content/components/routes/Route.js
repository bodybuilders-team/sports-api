import {div, h1, h3} from "../../js/dom/domTags.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Route details component.
 * @param state application state
 * @param props component properties
 * @returns route component
 */
async function Route(state, props) {
    if (props == null)
        throw new LogError("Route props must not be null");

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
