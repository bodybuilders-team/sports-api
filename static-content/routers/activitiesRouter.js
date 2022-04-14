import {Router} from "../Router.js";
import Activities from "../components/activities/Activities.js";
import Activity from "../components/activities/Activity.js";
import ActivitiesUsers from "../components/activities/ActivitiesUsers.js";

const router = Router();

router.addHandler('/users', ActivitiesUsers);
router.addHandler('/:id', Activity);
router.addHandler('/', Activities);

export default router;