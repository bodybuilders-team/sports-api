import {a, div, h3, h5, br} from "../../js/dom/domTags.js";
import {API_BASE_URL} from "../../js/config.js";
import Activities from "../activities/Activities.js";

/**
 * User details page.
 * @param state application state
 * @returns user page
 */
async function User(state) {
    const user = await fetch(API_BASE_URL + "/users/" + state.params.id)
        .then(res => res.json());

    state.props.activities = await fetch(API_BASE_URL + "/users/" + state.params.id + "/activities")
        .then(res => res.json())
        .then(json => json.activities);

    return div(
        {class: "row justify-content-evenly"},
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body d-flex justify-content-center"},
                h3({class: "card-title"}, user.name),
                h5({class: "card-subtitle"}, user.email)
            ),
            await Activities(state)
        )
    );
}

export default User;
