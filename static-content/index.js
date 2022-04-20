import App from "./components/App.js";
import AppError, {isAppError} from "./components/AppError.js";

window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);

/**
 * Creates a state.
 * @returns state object
 */
function createState() {
    return {path: "/", currentPath: "/", params: {}};
}

/**
 * Renders a component.
 * @param component component to render
 */
function render(component) {
    const mainContent = document.getElementById("mainContent");
    mainContent.replaceChildren(component);
}

/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 */
function hashChangeHandler() {
    const path = window.location.hash.replace("#", "/");

    const state = createState()
    state.path = state.currentPath = path

    App(state)
        .then(render)
        .catch(error => {
            if (isAppError(error)) {
                AppError(state, error).then(render);
            } else {
                throw error;
            }
        });
}
