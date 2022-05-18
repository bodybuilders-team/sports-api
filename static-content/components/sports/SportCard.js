import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * SportCard component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - sport id
 * @param {String} props.name - sport name
 *
 * @returns {Promise<HTMLElement>}
 */
async function SportCard(state, props) {

    const {id, name} = props;

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#sports/${id}`}, name))
        )
    );
}

export default SportCard;