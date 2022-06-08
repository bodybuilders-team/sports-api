import {Router} from "./router.js";
import UserPage from "../pages/users/UserPage.js";
import UsersRankingsPage from "../pages/users/UsersRankingsPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";
import UsersRankingsSearchPage from "../pages/users/UsersRankingsSearchPage.js";

const router = Router();
const rankingsRouter = Router();

rankingsRouter.addHandler('/', UsersRankingsPage)
rankingsRouter.addHandler('/search', UsersRankingsSearchPage)
rankingsRouter.addDefaultHandler(NotFoundPage);

router.addHandler('/rankings', rankingsRouter);
router.addHandler('/:id', UserPage);
router.addDefaultHandler(NotFoundPage);

export default router;