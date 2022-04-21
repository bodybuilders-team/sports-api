import apiFetch from "../../js/apiFetch.js";
import Users from "../../components/users/Users.js";
import {div, h1} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "../../components/pagination/SkipPagination.js";

/**
 * Users page.
 * @param state application state
 * @returns users page
 */
async function UsersPage(state) {
    let {skip, limit} = state.query || {};
    skip = parseInt(skip) || 0;
    limit = parseInt(limit) || 10;

    const searchParams = new URLSearchParams();
    if (skip != null)
        searchParams.set("skip", skip);
    if (limit != null)
        searchParams.set("limit", limit);

    const {users, totalCount} = await apiFetch(`/users?${searchParams.toString()}`);

    const onPageChange = (page) => {
        const skip = (page - 1) * limit;
        window.location.href = `#users?skip=${skip}&limit=${limit}`;
    }

    return div(
        {class: "row justify-content-center"},
        h1({class: "app_icon"}, "Users"),
        Users(state, {users}),

        SkipLimitPaginate(state, {
            skip,
            limit,
            totalCount,
            onPageChange
        }),
    );
}

export default UsersPage;
