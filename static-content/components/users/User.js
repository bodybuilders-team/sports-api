import {div, h1, h3, h5} from "../../js/dom/domTags.js";
import {API_BASE_URL} from "../../js/config.js";
import Activities from "../activities/Activities.js";

/**
 * User details page.
 * @param state application state
 * @props props component props
 * @returns user page
 */
async function User(state, props) {
    let id = (state.params.uid === undefined) ? state.params.id : props.id;

    const user = await fetch(API_BASE_URL + "/users/" + id)
        .then(res => res.json());

    const activitiesProps = await fetch(API_BASE_URL + "/users/" + state.params.id + "/activities")
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

            div(
                {class: "row justify-content-evenly"},
                h1({class: "app-icon"}, "Activities"),
                await Activities(state, {activities: activitiesProps}),
            )
        )
    );
}

export default User;
