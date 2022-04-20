import apiFetch from "../../js/apiFetch.js";
import Sport from "../../components/sports/Sport.js";

/**
 * Sport details page.
 * @param state application state
 * @returns sport page
 */
async function SportPage(state) {
    if (state.params.id === undefined)
        throw new Error("Sport id must be defined")

    const id = state.params.id;
    const sport = await apiFetch(`/sports/${id}`);

    const activities = await apiFetch(`/sports/${id}/activities`)
        .then(json => json.activities);

    return Sport(
        state,
        {id: sport.id, name: sport.name, description: sport.description, activities}
    );
}

export default SportPage;
