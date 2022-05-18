import apiFetch from "../../js/apiFetch.js";
import Sport from "../../components/sports/Sport.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit, reloadHash} from "../../js/utils.js";

/**
 * @typedef Sport
 * @property {number} id - sport id
 * @property {string} name - sport name
 * @property {string} description - sport description
 * @property {number} uid - sport creator id
 */

/**
 * Sport details page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportPage(state) {

    const id = parseInt(state.params.id);
    if (isNaN(id))
        throw new LogError("Invalid param id");

    /**
     * @type {Sport}
     */
    const sport = await apiFetch(`/sports/${id}`);

    let {skip, limit} = getQuerySkipLimit(state.query, 0, 5);
    const activitiesData = await apiFetch(`/sports/${id}/activities?skip=${skip}&limit=${limit}`);

    activitiesData.skip = skip;
    activitiesData.limit = limit;

    /**
     * Callback to be called when sport is updated.
     * @param {boolean} modified true if sport was modified, false otherwise
     */
    function onSportUpdated(modified) {
        if (modified)
            reloadHash();
    }

    return Sport(
        state,
        {
            ...sport,
            activitiesData,
            onSportUpdated
        }
    );
}

export default SportPage;
