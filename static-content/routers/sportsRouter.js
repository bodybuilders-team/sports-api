import {Router} from "./router.js";
import SportsPage from "../pages/sports/SportsPage.js";
import SportPage from "../pages/sports/SportPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";

const router = Router();

router.addHandler('/', SportsPage);
router.addHandler('/:id', SportPage);
router.addDefaultHandler(NotFoundPage);

export default router;