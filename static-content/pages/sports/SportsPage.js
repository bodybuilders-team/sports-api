import apiFetch from "../../js/apiFetch.js";
import Sports from "../../components/sports/Sports.js";
import {div, h1} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "../../components/pagination/SkipPagination.js";

/**
 * Sports details page.
 * @param state application state
 * @returns sports page
 */
async function SportsPage(state) {
    let {skip, limit} = state.query || {};
    skip = parseInt(skip) || 0;
    limit = parseInt(limit) || 10;

    const searchParams = new URLSearchParams();
    if (skip != null)
        searchParams.set("skip", skip);
    if (limit != null)
        searchParams.set("limit", limit);

    const {sports, totalCount} = await apiFetch(`/sports?${searchParams.toString()}`);

    const onPageChange = (page) => {
        const skip = (page - 1) * limit;
        window.location.href = `#sports?skip=${skip}&limit=${limit}`;
    }

    return div(
        h1({class: "app_icon"}, "Sports"),
        Sports(state, {sports}),

        SkipLimitPaginate(state, {
            skip,
            limit,
            totalCount,
            onPageChange
        }),
    );
}


export default SportsPage;
