import {div} from "../../js/dom/domTags.js";
import UserCard from "./UserCard.js";

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
            UserCard(state, user)
        )
    );
}

export default Users;