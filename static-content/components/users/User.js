import {div, h1, h3, h5} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * User details page.
 * @param state application state
 * @props props component props
 * @returns user page
 */
async function User(state, props) {
    let id = (state.params.id !== undefined) ? state.params.id : props.id;

    const user = await apiFetch(`users/${id}`);

    const activitiesProps = await apiFetch(`users/${id}/activities`)
        .then(json => json.activities);

    return div(
        div(
            div(
                h3(user.name),
                h5(user.email)
            ),
            div(
                h1("Activities"),
                Activities(state, {activities: activitiesProps}),
            )
        )
    );
}

export default User;
