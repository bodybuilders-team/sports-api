import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropUser
 * @property {number} id user id
 * @property {string} name user name
 */

/**
 * Users component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {PropUser[]} props.users - users Ids
 *
 * @return Promise<HTMLElement>
 */
async function Users(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        ...props.users.map(user =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#users/${user.id}`}, user.name))
                )
            )
        )
    );
}

export default Users;