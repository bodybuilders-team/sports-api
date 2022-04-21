import {a, div, h5} from "../../js/dom/domTags.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Routes component.
 * @param state application state
 * @param props component properties
 * @returns routes component
 */
async function Routes(state, props) {
    if (props == null)
        throw new LogError("Routes props must not be null");

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