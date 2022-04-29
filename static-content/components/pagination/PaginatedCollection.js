import {div} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "./SkipPaginate.js";

/**
 * Paginated collection component for a specific collection (ex: Sports, Activities...)
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.skip - number of elements to skip
 * @param {number} props.limit- number of elements per page
 * @param {Component} props.collectionComponent - component to visualize the paginated collection
 * @param {Array<Object>} props.collection - paginated collection data
 * @param {number} props.totalCount - total number of elements
 *
 * @return Promise<HTMLElement>
 */
async function PaginatedCollection(state, props) {
    const {skip, limit, collectionComponent, collectionName, collection, totalCount} = props;

    const urlSearchParams = new URLSearchParams(state.query);

    /** @type PageChangeCallback */
    const onPageChange = (page) => {
        const skip = (page - 1) * limit;

        urlSearchParams.set("skip", skip.toString());
        urlSearchParams.set("limit", limit.toString());

        window.location.hash = `#${state.path.substring(1)}?${urlSearchParams.toString()}`;
    }

    return div(
        {class: "row justify-content-center"},
        (totalCount <= 0) ?
            div({class: "alert alert-info ", role: "alert"}, `No ${collectionName} found.`,)
            : collectionComponent(state, {[collectionName]: collection}),

        (totalCount > limit) ? SkipLimitPaginate(state, {
            skip,
            limit,
            totalCount,
            onPageChange
        }) : undefined
    );
}

export default PaginatedCollection;
