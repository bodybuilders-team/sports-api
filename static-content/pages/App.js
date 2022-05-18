import {Router} from "../routers/router.js";
import usersRouter from "../routers/usersRouter.js";
import sportsRouter from "../routers/sportsRouter.js";
import routesRouter from "../routers/routesRouter.js";
import activitiesRouter from "../routers/activitiesRouter.js";
import HomePage from "./HomePage.js";
import NotFoundPage from "./errors/NotFoundPage.js";
import RegisterPage from "./RegisterPage.js";
import LoginPage from "./LoginPage.js";
import {getStoredUser} from "../js/utils.js";
import {a, div, hr, img, nav} from "../js/dom/domTags.js";
import LogoutDropdown from "../components/LogoutDropdown.js";

const router = Router();

router.addHandler("/", HomePage);
router.addHandler("/register", RegisterPage);
router.addHandler("/login", LoginPage);
router.addHandler("/users", usersRouter);
router.addHandler("/sports", sportsRouter);
router.addHandler("/routes", routesRouter);
router.addHandler("/activities", activitiesRouter);
router.addDefaultHandler(NotFoundPage);

/**
 * Initializes the Web application.
 *
 * @param {Object} state - application state
 * @returns Promise<HTMLElement>
 */
async function App(state) {
    const user = getStoredUser();


    return div(
        nav({class: "nav nav-pills"},
            a({class: "navbar-brand", href: "#"}, img({
                alt: "Sports API ICON",
                src: "/public/sports-api-icon.png",
                width: "40"
            })),
            a({class: "nav-link", href: "#"}, "Home"),
            a({class: "nav-link", href: "#users/search"}, "UsersRankings"),
            a({class: "nav-link", href: "#sports"}, "Sports"),
            a({class: "nav-link", href: "#activities/search"}, "ActivitiesSearch"),
            a({class: "nav-link", href: "#routes"}, "Routes"),

            user == null ?
                div({class: "nav ms-auto"},
                    a({class: "nav-item nav-link", href: "#register"}, "Register"),
                    a({class: "nav-item nav-link", href: "#login"}, "Login")
                ) :
                div({class: "nav ms-auto"},
                    LogoutDropdown(state),
                    a({class: "nav-item nav-link", href: `#users/${user.uid}`}, "Profile")
                )
        ),
        hr(),
        router(state)
    );
}

export default App;