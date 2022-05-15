import Sports from "../../components/sports/Sports.js";
import {a, br, div, h1, h5} from "../../js/dom/domTags.js";
import {alertBoxWithError, reloadHash} from "../../js/utils.js";
import CreateSport from "../../components/sports/CreateSport.js";
import apiFetch from "../../js/apiFetch.js";
import InfinitePaginate from "../../components/pagination/InfinitePaginate.js";

/**
 * Sports details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportsPage(state) {
    let totalCount = null
    let currentSkip = 0

    async function appendSports(numberSports) {
        if (totalCount != null && currentSkip + 1 >= totalCount)
            return

        const {
            sports,
            totalCount: newTotalCount,
        } = await apiFetch(`/sports?skip=${currentSkip}&limit=${numberSports}`)

        totalCount = newTotalCount
        currentSkip += numberSports

        return Promise.all(sports.map(sport =>
            div(
                {class: "card user-card col-6 bg-light"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#sports/${sport.id}`}, sport.name))
                )
            )
        ))
    }


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
            "http://localhost:8888/api/sports",
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

        if (res.ok)
            reloadHash()
        else
            await alertBoxWithError(state, form, json);
    }


    return div(
        h1({class: "app-icon"}, "Sports"),
        CreateSport(state, {onCreateSubmit: createSport}),
        br(),
        div(
            {class: "row justify-content-center"},
            InfinitePaginate(state, {
                onLoadMore: appendSports,
                initialNumChildren: 10,
                numChildren: 5
            })
        ),
    )
}


export default SportsPage;
