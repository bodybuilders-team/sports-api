import {Router} from "../Router.js";
import SportsPage from "../pages/sports/SportsPage.js";
import SportPage from "../pages/sports/SportPage.js";

const router = Router();

router.addHandler('/', SportsPage);
router.addHandler('/:id', SportPage);

export default router;