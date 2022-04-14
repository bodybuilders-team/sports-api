import {Router} from "../Router.js";
import Users from "../components/users/Users.js";
import User from "../components/users/User.js";
import UserActivities from "../components/users/UserActivities.js";

const router = Router();

router.addHandler('/', Users);
router.addHandler('/:id', User);
router.addHandler('/:id/activities', UserActivities);

export default router;