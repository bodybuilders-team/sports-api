import {a, div, h1, h5} from "../../js/dom/domTags.js";

/**
 * Users page.
 * @param state application state
 * @param props component properties
 * @returns users page
 */
async function Users(state, props) {
    if (props == null)
        throw new Error("Users props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Users"),
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