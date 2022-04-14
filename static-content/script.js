import App from "./components/App.js";

window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);


function render(component) {
    const mainContent = document.getElementById("mainContent");
    mainContent.replaceChildren(component);
}

/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 *
 */
function hashChangeHandler() {
    const path = window.location.hash.replace("#", "/");

    const state = {path: path, currentPath: path, params: {}};
    render(
        App(state)
    )
}
