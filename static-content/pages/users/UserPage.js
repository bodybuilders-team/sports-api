import apiFetch from "../../js/apiFetch.js";
import User from "../../components/users/User.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit} from "../../js/utils.js";

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

    let {skip, limit} = getQuerySkipLimit(state.query, 0, 10);

    const activitiesData = await apiFetch(`/users/${id}/activities?skip=${skip}&limit=${limit}`);

    activitiesData.skip = skip;
    activitiesData.limit = limit;

    return User(
        state,
        {id: user.id, name: user.name, email: user.email, activitiesData}
    );
}

export default UserPage;
