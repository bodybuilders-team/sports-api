import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * Activities component.
 * @param state application state
 * @param props component properties
 * @returns activities component
 */
async function Activities(state, props) {
    if (props == null)
        throw new Error("Activities props must not be null");

    return div(
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