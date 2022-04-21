import apiFetch from "../../js/apiFetch.js";
import User from "../../components/users/User.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * User details page.
 * @param state application state
 * @returns user page
 */
async function UserPage(state) {
    if (state.params.id === undefined)
        throw new LogError("User id must be defined")

    const id = state.params.id;
    const user = await apiFetch(`/users/${id}`);

    const activities = await apiFetch(`/users/${id}/activities`)
        .then(json => json.activities);

    return User(
        state,
        {id: user.id, name: user.name, email: user.email, activities}
    );
}

export default UserPage;
