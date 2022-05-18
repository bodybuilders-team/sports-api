import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * UserCard component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - user id
 * @param {String} props.name - user name
 *
 * @returns {Promise<HTMLElement>}
 */
async function UserCard(state, props) {

    const {id, name} = props;

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#users/${id}`}, name))
        )
    );
}

export default UserCard;
