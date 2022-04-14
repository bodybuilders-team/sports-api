import Home from "./Home.js";
import usersRouter from "../routers/usersRouter.js";
import {Router} from "../router.js";
import NotFound from "./NotFound.js";

function App(state) {
    const router = Router();

    router.addHandler("/users/api", usersRouter);
    router.addHandler("/users", usersRouter);
    router.addHandler("/home", Home);
    router.addHandler("/", Home);
    router.addDefaultHandler(NotFound);

    return router(state);
}

export default App;