import {a, div, nav} from "../js/dom/domTags.js";
import LogoutDropdown from "./LogoutDropdown.js";
import {getStoredUser} from "../js/utils.js";

/**
 * NavBar component.
 *
 * @param {Object} state - application state
 *
 * @return Promise<HTMLElement>
 */
async function NavBar(state) {

    const user = getStoredUser();

    const navbar = await div(
        nav(
            {class: "nav nav-pills"},
            a({class: "nav-link", href: "#"}, "Home"),
            a({class: "nav-link", href: "#users/rankings/search"}, "UsersRankings"),
            a({class: "nav-link", href: "#sports"}, "Sports"),
            a({class: "nav-link", href: "#activities/search",}, "ActivitiesSearch"),
            a({class: "nav-link", href: "#routes"}, "Routes"),

            user == null
                ? div({class: "nav ms-auto"},
                    a({class: "nav-item nav-link", href: "#register"}, "Register"),
                    a({class: "nav-item nav-link", href: "#login"}, "Login")
                )
                : div({class: "nav ms-auto"},
                    LogoutDropdown(state, {}),
                    a({class: "nav-item nav-link", href: `#users/${user.uid}`}, "Profile")
                )
        )
    );

    let active = window.location.hash;
    if (active === "")
        active = "#";

    const selectedNav = navbar.querySelector(`a[href="${active}"]`)
    if(selectedNav != null)
        selectedNav.classList.add("active");

    return navbar;
}

export default NavBar;
