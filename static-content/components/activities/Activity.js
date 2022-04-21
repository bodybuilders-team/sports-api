import {a, div, h1, h3, h5} from "../../js/dom/domTags.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Activity details component.
 * @param state application state
 * @param props component properties
 * @returns activity component
 */
async function Activity(state, props) {
    if (props == null)
        throw new LogError("Activity props must not be null");

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
