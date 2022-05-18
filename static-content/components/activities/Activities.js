import {div} from "../../js/dom/domTags.js";
import ActivityCard from "./ActivityCard.js";

/**
 * @typedef PropActivity
 * @property {number} id - activity id
 * @property {string} date - activity date
 */

/**
 * Activities component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {PropActivity[]} props.activities - activities
 *
 * @return Promise<HTMLElement>
 */
async function Activities(state, props) {

    const {activities} = props;

    return div(
        {class: "row justify-content-evenly"},
        ...activities.map(activity =>
            ActivityCard(state, activity)
        )
    );
}

export default Activities;