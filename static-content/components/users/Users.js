import {a, div, h5} from "../../js/dom/domTags.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Users page.
 * @param state application state
 * @param props component properties
 * @returns users page
 */
async function Users(state, props) {
    if (props == null)
        throw new LogError("Users props must not be null");

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