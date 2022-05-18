import {div} from "../../js/dom/domTags.js";

/**
 * @callback LoadMoreCallback
 * @param {number} numberOfItems - number of items to load
 */

/**
 * InfinitePaginate component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {LoadMoreCallback} props.onLoadMore - callback to load more items
 * @param {number} props.initialNumChildren - number of children to initially render
 * @param {number} props.numChildren - number of children to render after each load
 *
 * @return Promise<HTMLElement>
 */
async function InfinitePaginate(state, props) {

    const {onLoadMore, initialNumChildren, numChildren} = props;

    const container = await div({class: "row justify-content-evenly"});

    let loading = false;

    window.addEventListener("scroll", async () => {
        const {top} = container.lastElementChild.getBoundingClientRect();
        if (top < window.innerHeight && !loading) {
            loading = true;

            const children = await Promise.all(await onLoadMore(numChildren));
            container.append(...children);

            loading = false;
        }
    }, false);

    const children = await Promise.all(await onLoadMore(initialNumChildren));
    container.append(...children);

    return container;
}

export default InfinitePaginate;