import {API_BASE_URL} from "../../js/config.js";
import {div, h1, h5, a} from "../../js/dom/domTags.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function Users(state) {
    const users = await fetch(API_BASE_URL + "/users")
        .then(res => res.json())
        .then(json => json.users);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, "Users"),
        ...users.map(user =>
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