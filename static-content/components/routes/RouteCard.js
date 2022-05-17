import {a, div, h5} from "../../js/dom/domTags.js";

async function RouteCard(state, props) {
    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#routes/${props.id}`}, props.startLocation + " - " + props.endLocation)),
        )
    )
}

export default RouteCard;