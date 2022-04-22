import App from "./pages/App.js";
import {InvSearchParamsError, isAppError} from "./js/errorUtils.js";
import AppErrorPage from "./pages/errors/AppErrorPage.js";
import {createState, render} from "./js/compLib.js";
import InvSearchParamsErrorPage from "./pages/errors/InvSearchParamsErrorPage.js";

window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);


function handleComponentError(state, error) {
    if (error instanceof InvSearchParamsError)
        InvSearchParamsErrorPage(state, error).then(render);

    else if (isAppError(error))
        AppErrorPage(state, error).then(render);

    else
        throw error;
}

/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 */
function hashChangeHandler() {
    const path = window.location.hash.replace("#", "/");

    const state = createState(path);

    App(state)
        .then(render)
        .catch((error) => handleComponentError(state, error));
}
