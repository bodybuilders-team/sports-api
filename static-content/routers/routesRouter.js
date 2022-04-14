import {Router} from "../Router.js";
import Routes from "../components/routes/Routes.js";
import Route from "../components/routes/Route.js";

const router = Router();

router.addHandler('/', Routes);
router.addHandler('/:id', Route);

export default router;