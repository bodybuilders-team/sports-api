import apiFetch from "../../js/apiFetch.js";
import User from "../../components/users/User.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit} from "../../js/utils.js";

/**
 * @typedef User
 * @property {number} id - user unique identifier
 * @property {string} name - username
 * @property {string} email - user email
 */

/**
 * User page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function UserPage(state) {

    const id = state.params.id;
    if (isNaN(id))
        throw new LogError("Invalid param id");

    /**
     * @type {User}
     */
    const user = await apiFetch(`/users/${id}`);

    const {skip, limit} = getQuerySkipLimit(state.query, 0, 5);
    const activitiesData = await apiFetch(`/users/${id}/activities?skip=${skip}&limit=${limit}`);

    activitiesData.skip = skip;
    activitiesData.limit = limit;

    return User(
        state,
        {
            ...user,
            activitiesData
        }
    );
}

export default UserPage;
