import Paginate from "./Paginate.js";


/**
 * Pagination component based on skip and limit
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {PageChangeCallback} props.onPageChange - callback function to be called when page is changed
 * @param {number} props.skip - number of elements to skip
 * @param {number} props.limit- number of elements per page
 * @param {number} props.totalCount - total number of elements
 * @param {number} [props.pagesToShow=5] - number of pages to show
 *
 * @return Promise<HTMLElement>
 */
async function SkipLimitPaginate(state, props) {
    const {onPageChange, skip, limit, totalCount, pagesToShow} = props

    return Paginate(state, {
        page: Math.floor(skip / limit) + 1,
        pagesToShow: pagesToShow || 5,
        pagesCount: Math.ceil(totalCount / limit),
        onPageChange
    })
}

export default SkipLimitPaginate