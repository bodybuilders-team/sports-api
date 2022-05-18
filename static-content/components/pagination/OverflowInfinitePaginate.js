import {div} from "../../js/dom/domTags.js";

// TODO: Check comments
/**
 * OverflowInfinitePaginate component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {LoadMoreCallback} props.onLoadMore - callback to load more items
 * @param {number} props.initialNumChildren - number of children to initially render
 * @param {number} props.numChildren - number of children to render after each load
 * @param {} props.overflowHeight - height of overflow container
 * @param {} props.resetRef - ref to reset scroll position
 *
 * @return Promise<HTMLElement>
 */
async function OverflowInfinitePaginate(state, props) {

    const {
        onLoadMore,
        initialNumChildren,
        numChildren,
        overflowHeight,
        resetRef
    } = props;

    const container = await div({style: {height: overflowHeight, overflowY: "scroll"}});

    let loading = false;

    container.addEventListener("scroll", async () => {
        const lastElement = container.lastElementChild;
        if (lastElement == null)
            return;

        const {top: lastEleTop} = lastElement.getBoundingClientRect();
        const {bottom: containerBottom} = container.getBoundingClientRect();
        if (lastEleTop < containerBottom && !loading) {
            loading = true;

            const children = await Promise.all(await onLoadMore(numChildren));
            container.append(...children);

            loading = false;
        }
    }, false);

    // TODO: comment
    async function reset() {
        if (loading)
            return;

        loading = true;
        container.innerHTML = "";

        const children = await Promise.all(await onLoadMore(initialNumChildren));
        container.append(...children);

        loading = false;
    }

    resetRef.resolve(reset);

    await reset();

    return container;
}

export default OverflowInfinitePaginate;
