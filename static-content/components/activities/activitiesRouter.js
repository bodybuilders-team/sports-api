import {Router} from "../../Router.js";
import Activities from "./Activities.js";
import Activity from "./Activity.js";
import ActivitiesUsers from "./ActivitiesUsers.js";

const router = Router();

router.addHandler('/', Activities);
router.addHandler('/users', ActivitiesUsers);
router.addHandler('/:id', Activity);

export default router;