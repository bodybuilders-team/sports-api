import {Router} from "../Router.js";
import UserPage from "../pages/users/UserPage.js";
import UsersPage from "../pages/users/UsersPage.js";

const router = Router();

router.addHandler('/', UsersPage);
router.addHandler('/:id', UserPage);

export default router;