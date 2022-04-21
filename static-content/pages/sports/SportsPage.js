import Sports from "../../components/sports/Sports.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {div, h1} from "../../js/dom/domTags.js";

/**
 * Sports details page.
 * @param state application state
 * @returns sports page
 */
async function SportsPage(state) {

    return div(
        h1({class: "app_icon"}, "Sports"),
        FetchedPaginatedCollection(state,
            {
                initialSkip: 0,
                initialLimit: 10,
                collectionComponent: Sports,
                collectionEndpoint: "/sports",
                collectionName: "sports",
            }
        )
    )
}


export default SportsPage;
