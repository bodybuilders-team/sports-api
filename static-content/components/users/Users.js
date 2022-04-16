import {a, div, h1, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function Users(state) {
    const users = await apiFetch(`users`)
        .then(json => json.users);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Users"),
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