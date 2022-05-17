import {Router} from "./router.js";
import UserPage from "../pages/users/UserPage.js";
import UsersPage from "../pages/users/UsersPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";
import UsersRankingsSearchPage from "../pages/users/UsersRankingsSearchPage.js";

const router = Router();

router.addHandler('/', UsersPage);
router.addHandler('/search', UsersRankingsSearchPage)
router.addHandler('/:id', UserPage);
router.addDefaultHandler(NotFoundPage);

export default router;