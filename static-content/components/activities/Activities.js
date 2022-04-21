import {a, div, h5} from "../../js/dom/domTags.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Activities component.
 * @param state application state
 * @param props component properties
 * @returns activities component
 */
async function Activities(state, props) {
    if (props == null)
        throw new LogError("Activities props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        ...props.activities.map(activity =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5(
                        {class: "card-title"},
                        a({href: `#activities/${activity.id}`}, `Activity ${activity.id}`)
                    )
                )
            )
        )
    );
}

export default Activities;