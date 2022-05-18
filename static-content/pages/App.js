import {Router} from "../routers/router.js";
import usersRouter from "../routers/usersRouter.js";
import sportsRouter from "../routers/sportsRouter.js";
import routesRouter from "../routers/routesRouter.js";
import activitiesRouter from "../routers/activitiesRouter.js";
import HomePage from "./HomePage.js";
import NotFoundPage from "./errors/NotFoundPage.js";
import RegisterPage from "./RegisterPage.js";
import LoginPage from "./LoginPage.js";

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
    return router(state);
}

export default App;