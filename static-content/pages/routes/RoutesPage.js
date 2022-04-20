import apiFetch from "../../js/apiFetch.js";
import Routes from "../../components/routes/Routes.js";

/**
 * Routes page.
 * @param state application state
 * @returns routes page
 */
async function RoutesPage(state) {
    const routes = await apiFetch(`/routes`)
        .then(json => json.routes);

    return Routes(state, {routes});
}

export default RoutesPage;
