import {div} from "../../js/dom/domTags.js";
import ActivityCard from "./ActivityCard.js";

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
            ActivityCard(state, activity)
        )
    )
}

export default Activities;