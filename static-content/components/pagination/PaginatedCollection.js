import {div} from "../../js/dom/domTags.js";
import SkipLimitPaginate from "./SkipPaginate.js";

async function PaginatedCollection(state, props) {
    const {skip, limit, collectionComponent, collectionName, collection, totalCount} = props;

    const urlSearchParams = new URLSearchParams(state.query);

    const onPageChange = (page) => {
        const skip = (page - 1) * limit;

        urlSearchParams.set("skip", skip);
        urlSearchParams.set("limit", limit);

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
