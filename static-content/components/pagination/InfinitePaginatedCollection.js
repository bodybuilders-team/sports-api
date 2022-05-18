import {createRef} from "../../js/utils.js";
import apiFetch from "../../js/apiFetch.js";
import {div} from "../../js/dom/domTags.js";
import InfinitePaginate from "./InfinitePaginate.js";

// TODO comment
async function InfinitePaginatedCollection(state, props) {
    let {collectionComponent, collectionEndpoint, collectionName, searchParams} = props;
    const containerRef = createRef();

    const urlSearchParams = new URLSearchParams(searchParams || {});

    let totalCount = null;
    let currentSkip = 0;

    async function onLoadMore(numberOfItems) {
        if (totalCount != null && currentSkip + 1 >= totalCount)
            return [];

        urlSearchParams.set("skip", currentSkip);
        urlSearchParams.set("limit", numberOfItems);

        const {
            [collectionName]: items,
            totalCount: newTotalCount,
        } = await apiFetch(`${collectionEndpoint}?${urlSearchParams.toString()}`);


        if (currentSkip === 0 && newTotalCount === 0) {
            const container = await containerRef;

            const alertBox = await div({class: "alert alert-info w-50", role: "alert"}, `No ${collectionName} found.`);

            container.replaceChildren(alertBox);

            return [];
        }

        totalCount = newTotalCount;
        currentSkip += numberOfItems;

        return items.map(item =>
            collectionComponent(state, item)
        );
    }

    return div(
        {class: "row justify-content-center", ref: containerRef},
        InfinitePaginate(state, {
            onLoadMore,
            initialNumChildren: 10,
            numChildren: 5
        })
    );
}


export default InfinitePaginatedCollection;
