import Sports from "../../components/sports/Sports.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Sports details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportsPage(state) {

    return div(
        h1({class: "app_icon"}, "Sports"),
        FetchedPaginatedCollection(state,
            {
                defaultSkip: 0,
                defaultLimit: 10,
                collectionComponent: Sports,
                collectionEndpoint: "/sports",
                collectionName: "sports",
            }
        )
    );
}


export default SportsPage;
