export function range(startNumber, endNumber) {
    return Array(endNumber - startNumber + 1)
        .fill(0)
        .map((_, index) => startNumber + index);
}

export function getQuerySkipLimit(query, defaultSkip, defaultLimit) {
    let {skip, limit} = query;
    skip = parseInt(skip) || defaultSkip;
    limit = parseInt(limit) || defaultLimit;

    return {skip, limit}
}