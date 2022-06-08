import {createRef} from "../../js/utils.js";
import apiFetch from "../../js/apiFetch.js";
import {div} from "../../js/dom/domTags.js";
import InfinitePaginate from "./InfinitePaginate.js";


/**
 * InfinitePaginatedCollection component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {Component} props.collectionComponent - component to visualize the paginated collection
 * @param {String} props.collectionEndpoint - endpoint to fetch collection from
 * @param {String} props.collectionName - name of collection
 * @param {Object} props.searchParams - search parameters to pass to collection endpoint
 *
 * @return Promise<HTMLElement>
 */
async function InfinitePaginatedCollection(state, props) {
    const {collectionComponent, collectionEndpoint, collectionName, searchParams, extraPropData} = props;
    const containerRef = createRef();

    const urlSearchParams = new URLSearchParams(searchParams || {});

    let totalCount = null;
    let currentSkip = 0;

    /**
     * Callback to load more items.
     * @param numberOfItems - number of items to load
     * @return Promise<Array<HTMLElement>>
     */
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

        return items.map(item => {
            if (extraPropData != null) {
                for (const extraData in extraPropData)
                    item[extraData] = extraPropData[extraData]
            }
            return collectionComponent(state, item)
        });
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
