import apiFetch from "../../js/apiFetch.js";
import Sports from "../../components/sports/Sports.js";

/**
 * Sports details page.
 * @param state application state
 * @returns sports page
 */
async function SportsPage(state) {
    const sports = await apiFetch(`/sports`)
        .then(json => json.sports);

    return Sports(state, {sports});
}

export default SportsPage;
