/**
 * Creates a state.
 * @returns state object
 */
export function createState() {
    return {path: "/", currentPath: "/", params: {}, query: {}};
}

/**
 * Changes state path and currentPath.
 */
export function changeStatePath(state, newPath) {
    state.path = state.currentPath = newPath
}

/**
 * Renders a component.
 * @param component component to render
 */
export function render(component) {
    const mainContent = document.getElementById("mainContent");
    mainContent.replaceChildren(component);
}
