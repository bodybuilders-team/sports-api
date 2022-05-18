import {br, div, h1} from "../../js/dom/domTags.js";
import CreateSport from "../../components/sports/CreateSport.js";
import InfinitePaginatedCollection from "../../components/pagination/InfinitePaginatedCollection.js";
import SportCard from "../../components/sports/SportCard.js";

/**
 * Sports details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportsPage(state) {

    /**
     * Callback for when a sport is created.
     * @param sport - the sport that was created
     */
    function onSportCreated(sport) {
        window.location.hash = "#sports/" + sport.id;
    }

    return div(
        h1({class: "app-icon"}, "Sports"),
        CreateSport(state, {onSportCreated}),
        br(),

        InfinitePaginatedCollection(state, {
            collectionComponent: SportCard,
            collectionEndpoint: "/sports",
            collectionName: "sports"
        })
    );
}


export default SportsPage;
