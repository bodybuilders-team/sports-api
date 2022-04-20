import {Router} from "../Router.js";
import SearchActivitiesPage from "../pages/activities/SearchActivitiesPage.js";
import ActivityPage from "../pages/activities/ActivityPage.js";

const router = Router();

router.addHandler('/', SearchActivitiesPage);
router.addHandler('/:id', ActivityPage);

export default router;