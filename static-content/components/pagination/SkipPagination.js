import Paginate from "./Paginate.js";

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