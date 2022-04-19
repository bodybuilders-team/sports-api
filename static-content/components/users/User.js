import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * User details page.
 * @param state application state
 * @param props component properties
 * @returns user page
 */
async function User(state, props) {
    const id = (state.params.id !== undefined)
        ? state.params.id
        : props.id;

    const user = (props != null && props.sport != null)
        ? props.user
        : await apiFetch(`users/${id}`);

    const activitiesProps = await apiFetch(`users/${id}/activities`)
        .then(json => json.activities);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `User ${user.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Username: ", user.name),
                h3("Email: ", user.email),
                h3("Activities:"),
                Activities(state, {activities: activitiesProps})
            )
        )
    );
}

export default User;
