import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * ActivityCard component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {string} props.date - activity date
 *
 * @returns {Promise<HTMLElement>}
 */
async function ActivityCard(state, props) {

    const {id, date} = props;

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#activities/${id}`}, date))
        )
    );
}

export default ActivityCard;