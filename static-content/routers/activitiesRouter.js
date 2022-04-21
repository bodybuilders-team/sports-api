import {Router} from "../router.js";
import SearchActivitiesPage from "../pages/activities/SearchActivitiesPage.js";
import ActivityPage from "../pages/activities/ActivityPage.js";
import NotFoundPage from "../pages/NotFoundPage.js";

const router = Router();

router.addHandler('/', SearchActivitiesPage);
router.addHandler('/:id', ActivityPage);
router.addDefaultHandler(NotFoundPage);

export default router;