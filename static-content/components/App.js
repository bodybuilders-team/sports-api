import {Router} from "../Router.js";
import usersRouter from "./users/usersRouter.js";
import sportsRouter from "./sports/sportsRouter.js";
import routesRouter from "./routes/routesRouter.js";
import activitiesRouter from "./activities/activitiesRouter.js";
import Home from "./Home.js";
import NotFound from "./NotFound.js";

const router = Router();

router.addHandler("/", Home);
router.addHandler("/users", usersRouter);
router.addHandler("/sports", sportsRouter);
router.addHandler("/routes", routesRouter);
router.addHandler("/activities", activitiesRouter);
router.addDefaultHandler(NotFound);

/**
 * Initializes the Web application.
 * @param state application state
 * @returns application
 */
async function App(state) {
    return router(state);
}

export default App;