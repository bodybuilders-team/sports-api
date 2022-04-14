import {Router} from "../Router.js";
import usersRouter from "../routers/usersRouter.js";
import sportsRouter from "../routers/sportsRouter.js";
import routesRouter from "../routers/routesRouter.js";
import activitiesRouter from "../routers/activitiesRouter.js";
import Home from "./Home.js";
import NotFound from "./NotFound.js";

/**
 * Initializes the Web application.
 * @param state application state
 * @returns application
 */
async function App(state) {
    const router = Router();

    router.addHandler("/users", usersRouter);
    router.addHandler("/sports", sportsRouter);
    router.addHandler("/routes", routesRouter);
    router.addHandler("/activities", activitiesRouter);
    router.addHandler("/home", Home);
    router.addHandler("/", Home);

    router.addDefaultHandler(NotFound);

    return router(state);
}

export default App;