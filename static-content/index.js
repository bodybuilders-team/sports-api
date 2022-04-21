import App from "./components/App.js";
import {isAppError} from "./js/errorUtils.js";
import AppErrorPage from "./pages/AppErrorPage.js";
import {changeStatePath, createState, render} from "./js/compLib.js";

window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);


/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 */
function hashChangeHandler() {
    const path = window.location.hash.replace("#", "/");

    const state = createState();
    changeStatePath(state, path);

    App(state)
        .then(render)
        .catch(error => {
            if (isAppError(error))
                AppErrorPage(state, error).then(render);
            else
                throw error;
        });
}
