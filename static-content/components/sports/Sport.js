import {div, h1, h3} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";
import Activities from "../activities/Activities.js";

/**
 * Sport details page.
 * @param state application state
 * @param props component properties
 * @returns sport page
 */
async function Sport(state, props) {
    const id = (state.params.id !== undefined)
        ? state.params.id
        : props.id;

    const sport = (props != null && props.sport != null)
        ? props.sport
        : await apiFetch(`sports/${id}`);

    const activitiesProps = await apiFetch(`sports/${sport.id}/activities`)
        .then(json => json.activities);

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Sport ${sport.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Name: ", sport.name),
                h3("Description: ", sport.description),
                h3("Activities:"),
                Activities(state, {activities: activitiesProps})
            )
        )
    );
}

export default Sport;
