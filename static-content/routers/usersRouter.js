import {Router} from "../router.js";
import UserPage from "../pages/users/UserPage.js";
import UsersPage from "../pages/users/UsersPage.js";
import NotFoundPage from "../pages/NotFoundPage.js";

const router = Router();

router.addHandler('/', UsersPage);
router.addHandler('/:id', UserPage);
router.addDefaultHandler(NotFoundPage);

export default router;