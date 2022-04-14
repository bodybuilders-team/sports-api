import App from "./components/App.js";

window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);

/**
 * Creates a state.
 * @returns state object
 */
function createState() {
    return {path: "/", currentPath: "/", params: {}, props: {}};
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

    App(state).then(app => render(app));
}
