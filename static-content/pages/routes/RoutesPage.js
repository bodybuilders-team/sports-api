import apiFetch from "../../js/apiFetch.js";
import Routes from "../../components/routes/Routes.js";
import {div, h1} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "../../components/pagination/SkipPagination.js";

/**
 * Routes page.
 * @param state application state
 * @returns routes page
 */
async function RoutesPage(state) {
    let {skip, limit} = state.query || {};
    skip = parseInt(skip) || 0;
    limit = parseInt(limit) || 10;

    const searchParams = new URLSearchParams();
    if (skip != null)
        searchParams.set("skip", skip);
    if (limit != null)
        searchParams.set("limit", limit);

    const {routes, totalCount} = await apiFetch(`/routes?${searchParams.toString()}`);

    const onPageChange = (page) => {
        const skip = (page - 1) * limit;
        window.location.href = `#routes?skip=${skip}&limit=${limit}`;
    }

    return div(
        h1({class: "app_icon"}, "Routes"),
        Routes(state, {routes}),

        SkipLimitPaginate(state, {
            skip,
            limit,
            totalCount,
            onPageChange,
        }),
    );
}


export default RoutesPage;
