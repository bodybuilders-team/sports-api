import {a, li, nav, span, ul} from "../../js/dom/domTags.js";
import {range} from "../../js/utils.js";

/**
 * @callback PageChangeCallback
 * @param {number} page - new page number
 */

/**
 * Pagination component.
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {PageChangeCallback} props.onPageChange - callback function to be called when page is changed
 * @param {number} props.page - current page
 * @param {number} props.pagesToShow - number of pages to show
 * @param {number} props.pagesCount - total number of pages
 *
 * @return Promise<HTMLElement>
 */
async function Paginate(state, props) {
    let {onPageChange, page, pagesToShow, pagesCount} = props;

    const leftPage = Math.max(1, page - Math.floor(pagesToShow / 2));
    const rightPage = Math.min(pagesCount, leftPage + pagesToShow - 1);

    const pages = range(leftPage, rightPage);

    function onLeftmostArrowClick() {
        onPageChange(1);
    }

    function onLeftArrowClick() {
        onPageChange(Math.max(page - 1, 1));
    }

    function onRawPageClick(event) {
        const newPage = parseInt(event.target.textContent);
        onPageChange(newPage);
    }

    function onRightArrowClick() {
        onPageChange(Math.min(page + 1, pagesCount));
    }

    function onRightmostArrowClick() {
        onPageChange(pagesCount);
    }

    // << < 1 2 3 > >>
    return nav(
        ul({class: "pagination justify-content-center"},
            li({class: "page-item"}, a({
                class: "page-link",
                onClick: onLeftmostArrowClick,
                ["aria-label"]: "First"
            }, span({["aria-hidden"]: "true"}, "«")),),

            li({class: "page-item"}, a({
                class: "page-link",
                onClick: onLeftArrowClick,
                ["aria-label"]: "Previous"
            }, span({["aria-hidden"]: "true"}, "<"))),

            ...pages.map(pageNum =>
                li({class: "page-item" + (pageNum === page ? " active" : "")},
                    a({
                        class: "page-link", onClick: (pageNum === page) ? undefined : onRawPageClick,
                        ["aria-label"]: "" + pageNum
                    }, "" + pageNum))),

            li({class: "page-item"}, a({
                    class: "page-link",
                    onClick: onRightArrowClick,
                    ["aria-label"]: "Next"
                }, span({["aria-hidden"]: "true"}, ">")),
            ),

            li({class: "page-item"}, a({
                    class: "page-link",
                    onClick: onRightmostArrowClick,
                    ["aria-label"]: "Last"
                }, span({["aria-hidden"]: "true"}, "»"))
            )
        )
    );
}

export default Paginate;