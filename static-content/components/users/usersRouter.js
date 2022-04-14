import {Router} from "../../Router.js";
import Users from "./Users.js";
import User from "./User.js";

const router = Router();

router.addHandler('/', Users);
router.addHandler('/:id', User);

export default router;