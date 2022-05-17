import {Router} from "./router.js";
import SearchActivitiesPage from "../pages/activities/SearchActivitiesPage.js";
import ActivityPage from "../pages/activities/ActivityPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";
import ActivitiesPage from "../pages/activities/ActivitiesPage.js";

const router = Router();

router.addHandler('/', ActivitiesPage);
router.addHandler('/search', SearchActivitiesPage);
router.addHandler('/:id', ActivityPage);
router.addDefaultHandler(NotFoundPage);

export default router;