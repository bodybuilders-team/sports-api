import apiFetch from "../../js/apiFetch.js";
import Users from "../../components/users/Users.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function UsersPage(state) {
    const users = await apiFetch(`/users`)
        .then(json => json.users);

    return Users(state, {users});
}

export default UsersPage;
