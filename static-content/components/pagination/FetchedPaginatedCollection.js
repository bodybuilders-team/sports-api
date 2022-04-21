import apiFetch from "../../js/apiFetch.js";
import PaginatedCollection from "./PaginatedCollection.js";
import {getQuerySkipLimit} from "../../js/utils.js";

async function FetchedPaginatedCollection(state, props) {
    let {initialSkip, initialLimit, collectionComponent, searchParams, collectionEndpoint, collectionName} = props;
    let {skip, limit} = getQuerySkipLimit(state.query, initialSkip, initialLimit);

    searchParams = searchParams || {};
    searchParams.skip = skip;
    searchParams.limit = limit;

    //Removes undefined and null values from the searchParams object (for the apiFetch call)
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
