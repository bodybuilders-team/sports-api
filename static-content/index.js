import App from "./pages/App.js";
import {InvalidSearchParamsError, isAppError} from "./js/errorUtils.js";
import AppErrorPage from "./pages/errors/AppErrorPage.js";
import {createState, render} from "./js/compLib.js";
import InvalidSearchParamsErrorPage from "./pages/errors/InvalidSearchParamsErrorPage.js";

window.addEventListener("load", renderApp);
window.addEventListener("hashchange", renderApp);

/**
 * Handles a component error.
 *
 * @param {Object} state - applicationState
 * @param {Object} error - error object
 */
function handleComponentError(state, error) {
    if (error instanceof InvalidSearchParamsError)
        InvalidSearchParamsErrorPage(state, error).then(render);
    else if (isAppError(error))
        AppErrorPage(state, error).then(render);
    else
        throw error;
}

/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 */
function renderApp() {
    const path = window.location.hash.replace("#", "/");

    const state = createState(path);

    App(state)
        .then(render)
        .catch((error) => handleComponentError(state, error));
}
