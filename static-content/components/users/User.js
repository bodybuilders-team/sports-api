import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";

/**
 * User details page.
 * @param state application state
 * @param props component properties
 * @returns user page
 */
async function User(state, props) {
    if (props == null)
        throw new Error("User props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `User ${props.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Username: ", props.name),
                h3("Email: ", props.email),
                h3("Activities:"),
                Activities(state, {activities: props.activities})
            )
        )
    );
}

export default User;
