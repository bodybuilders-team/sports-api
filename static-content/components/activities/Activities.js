import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropActivity
 * @property {number} id activity id
 */

/**
 * Activities component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {PropActivity[]} props.activities - activities Ids
 *
 * @return Promise<HTMLElement>
 */
async function Activities(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        ...props.activities.map(activity =>
            div(
                {class: "card user-card col-6"},
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