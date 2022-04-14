import {API_BASE_URL} from "../../js/config.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function Users(state) {
    return fetch(API_BASE_URL + "users")
        .then(res => res.json())
        .then(json => json.users)
        .then(users => {
            div(
                h1("Users")//,
                /*users.map(user => {
                    a({href: `#users/${user.id}`}, `User ${user.id}`)
                })*/
            )
        });
}

export default Users;