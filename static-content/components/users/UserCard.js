import {a, div, h5} from "../../js/dom/domTags.js";

async function UserCard(state, props) {
    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#users/${props.id}`}, props.name))
        )
    )
}

export default UserCard;
