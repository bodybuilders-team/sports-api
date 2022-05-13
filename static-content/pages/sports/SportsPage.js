import Sports from "../../components/sports/Sports.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import {br, div, h1} from "../../js/dom/domTags.js";
import {reloadHash} from "../../js/utils.js";
import CreateSport from "../../components/sports/CreateSport.js";

/**
 * Sports details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportsPage(state) {

    /**
     * Creates a sport.
     * @param event form event
     */
    async function createSport(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#sportName").value;
        const description = form.querySelector("#sportDescription").value;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/sports/",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({name, description})
            }
        );

        const json = await res.json();

        if (res.ok) {
            reloadHash();
            return;
        }

        const alertBox = form.parentNode.querySelector("#alert_box");
        alertBox
            ? alertBox.innerHTML = json.extraInfo
            : await form.parentNode.appendChild(
                await div(
                    br(),
                    div(
                        {id: "alert_box", class: "alert alert-warning", role: "alert"},
                        json.extraInfo
                    )
                )
            );
    }

    return div(
        h1({class: "app_icon"}, "Sports"),
        CreateSport(state, {onCreateSubmint: createSport}),
        br(),
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
