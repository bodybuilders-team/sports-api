import {div, h1, h3} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * Sport details page.
 * @param state application state
 * @returns sport page
 */
async function Sport(state, props) {
    const sport = (props != null && props.sport != null) ? props.sport
        : await apiFetch(`sports/${(state.params.id != null) ? state.params.id : props.id}`);

    return div(
        h1(sport.name),
        h3(sport.description)
    );
}

export default Sport;
