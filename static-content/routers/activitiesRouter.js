import {Router} from "../Router.js";
import Activities from "../components/activities/Activities.js";
import Activity from "../components/activities/Activity.js";
import ActivitiesUsers from "../components/activities/ActivitiesUsers.js";

const router = Router();

router.addHandler('/', Activities);
router.addHandler('/:id', Activity);
router.addHandler('/users', ActivitiesUsers);

export default router;