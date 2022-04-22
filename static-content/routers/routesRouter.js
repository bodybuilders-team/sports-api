import {Router} from "./router.js";
import RoutesPage from "../pages/routes/RoutesPage.js";
import RoutePage from "../pages/routes/RoutePage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";

const router = Router();

router.addHandler('/', RoutesPage);
router.addHandler('/:id', RoutePage);
router.addDefaultHandler(NotFoundPage);

export default router;