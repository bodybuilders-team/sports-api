import {Router} from "../../Router.js";
import Routes from "./Routes.js";
import Route from "./Route.js";

const router = Router();

router.addHandler('/', Routes);
router.addHandler('/:id', Route);

export default router;