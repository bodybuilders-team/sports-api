import {Router} from "../Router.js";
import RoutesPage from "../pages/routes/RoutesPage.js";
import RoutePage from "../pages/routes/RoutePage.js";

const router = Router();

router.addHandler('/', RoutesPage);
router.addHandler('/:id', RoutePage);

export default router;