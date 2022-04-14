import router from "./router.js";
import handlers from "./handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

/**
 * Loads handlers.
 *
 * @description
 */
function loadHandler(){
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("users", handlers.getUsers)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

/**
 * Called whenever the hash changes.
 * Calls the handler corresponding to the path from the hash.
 *
 */
function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent)
}