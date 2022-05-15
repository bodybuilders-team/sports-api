import {div} from "../../js/dom/domTags.js";

async function InfinitePaginate(state, props) {
    const {
        onLoadMore,
        initialNumChildren,
        numChildren
    } = props;


    const container = await div(
        {class: "row justify-content-evenly"},
    )

    let loading = false

    window.addEventListener("scroll", async () => {
        const {top} = container.lastElementChild.getBoundingClientRect();
        if (top < window.innerHeight && !loading) {
            loading = true

            const children = await onLoadMore(numChildren)
            container.append(...children)

            loading = false
        }
    }, false);

    const children = await onLoadMore(initialNumChildren)
    container.append(...children)

    return container
}

export default InfinitePaginate;