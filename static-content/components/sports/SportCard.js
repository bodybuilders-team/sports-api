import {a, div, h5} from "../../js/dom/domTags.js";

async function SportCard(state, props) {
    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#sports/${props.id}`}, props.name))
        )
    )
}

export default SportCard;