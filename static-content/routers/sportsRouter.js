import {Router} from "../Router.js";
import Sport from "../components/sports/Sport.js";
import Sports from "../components/sports/Sports.js";
import SportActivities from "../components/sports/SportActivities.js";

const router = Router();

router.addHandler('/', Sports);
router.addHandler('/:id', Sport);
router.addHandler('/:id/activities', SportActivities);

export default router;