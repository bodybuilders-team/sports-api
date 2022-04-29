import apiFetch from "../../js/apiFetch.js";
import Sport from "../../components/sports/Sport.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit} from "../../js/utils.js";

/**
 * Sport details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportPage(state) {
    if (state.params.id === undefined)
        throw new LogError("Sport id must be defined")


    const id = state.params.id;
    const sport = await apiFetch(`/sports/${id}`);

    let {skip, limit} = getQuerySkipLimit(state.query, 0, 10);

    const activitiesData = await apiFetch(`/sports/${id}/activities?skip=${skip}&limit=${limit}`);

    activitiesData.skip = skip;
    activitiesData.limit = limit;

    return Sport(
        state,
        {id: sport.id, name: sport.name, description: sport.description, activitiesData}
    );
}

export default SportPage;
