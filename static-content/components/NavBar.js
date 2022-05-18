import {a, div, img, nav} from "../js/dom/domTags.js";
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

    return div(
        nav(
            {class: "nav nav-pills"},
            a({class: "navbar-brand", href: "#"},
                img({
                    alt: "Sports API ICON",
                    src: "/public/sports-api-icon.png",
                    width: "40"
                })
            ),
            a({class: "nav-link active", href: "#"}, "Home"),
            a({class: "nav-link", href: "#users/search"}, "UsersRankings"),
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
}

export default NavBar;
