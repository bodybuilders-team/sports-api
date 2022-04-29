import apiFetch from "../../js/apiFetch.js";
import PaginatedCollection from "./PaginatedCollection.js";
import {getQuerySkipLimit} from "../../js/utils.js";

/**
 *
 * Paginated collection component for a specific collection (ex: Sports, Activities...)
 * that fetches it's own data through the api
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {?number=} props.defaultSkip - default skip to be used when it's not specified in search params
 * @param {?number=} props.defaultLimit - default limit to be used when it's not specified in search params
 * @param {Component} props.collectionComponent - component to visualize the paginated collection
 * @param {?Object=} props.searchParams - api fetch search parameters
 * @param {string} props.collectionEndpoint - HTTP endpoint for the collection
 * @param {string} props.collectionName - HTTP response collection attribute name
 *
 * @return Promise<HTMLElement>
 */
async function FetchedPaginatedCollection(state, props) {
    let {defaultSkip, defaultLimit, collectionComponent, searchParams, collectionEndpoint, collectionName} = props;
    let {skip, limit} = getQuerySkipLimit(state.query, defaultSkip, defaultLimit);

    searchParams = searchParams || {};
    searchParams.skip = skip;
    searchParams.limit = limit;

    // Removes undefined and null values from the searchParams object (for the apiFetch call)
    for (const key in searchParams) {
        if (searchParams[key] == null)
            delete searchParams[key];
    }

    const apiFetchParams = new URLSearchParams(searchParams);

    const {
        [collectionName]: collection,
        totalCount
    } = await apiFetch(`${collectionEndpoint}?${apiFetchParams.toString()}`);


    return PaginatedCollection(
        state,
        {
            skip,
            limit,
            collectionComponent,
            collectionName,
            collection,
            totalCount
        }
    );
}

export default FetchedPaginatedCollection;
