import Users from "../components/Users.js";
import {Router} from "../router.js";
import User from "../components/User.js";

const router = Router()

router.addHandler('/', Users)
router.addHandler('/:id', User)

export default router