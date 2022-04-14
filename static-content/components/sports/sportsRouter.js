import {Router} from "../../Router.js";
import Sport from "./Sport.js";
import Sports from "./Sports.js";

const router = Router();

router.addHandler('/', Sports);
router.addHandler('/:id', Sport);

export default router;