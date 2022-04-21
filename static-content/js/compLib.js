/**
 * Parses search query params from a path.
 * @param rawPath
 */
function parsePath(rawPath) {
    const query = {}

    const url = new URL(rawPath, window.location.origin)

    const path = url.pathname

    url.searchParams.forEach((value, key) => {
        query[key] = value;
    })

    return {path, query}
}

/**
 * Creates a state.
 * @returns state object
 */
export function createState(rawPath) {
    //Right now the state is just the path and query,
    // but in the future it could be more complex
    return parsePath(rawPath);
}

/**
 * Renders a component.
 * @param component component to render
 */
export function render(component) {
    const mainContent = document.getElementById("mainContent");
    mainContent.replaceChildren(component);
}
