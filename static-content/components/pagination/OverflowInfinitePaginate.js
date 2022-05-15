import {div} from "../../js/dom/domTags.js";

async function OverflowInfinitePaginate(state, props) {
    const {
        onLoadMore,
        initialNumChildren,
        numChildren,
        overflowHeight,
        resetRef
    } = props;

    const container = await div(
        {style: {height: overflowHeight, overflowY: "scroll"}}
    )

    let loading = false

    container.addEventListener("scroll", async () => {
        const lastElement = container.lastElementChild;
        if (lastElement == null)
            return;

        const {top: lastEleTop} = lastElement.getBoundingClientRect();
        const {bottom: containerBottom} = container.getBoundingClientRect();
        if (lastEleTop < containerBottom && !loading) {
            loading = true

            const children = await onLoadMore(numChildren)
            container.append(...children)

            loading = false
        }
    }, false);

    async function reset() {
        if (loading)
            return;

        loading = true
        container.innerHTML = ""

        const children = await onLoadMore(initialNumChildren)
        container.append(...children)

        loading = false
    }

    resetRef.resolve(reset)

    await reset()

    return container
}

export default OverflowInfinitePaginate;
