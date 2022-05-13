import apiFetch from "../../js/apiFetch.js";
import Sport from "../../components/sports/Sport.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit, reloadHash} from "../../js/utils.js";
import {br, div} from "../../js/dom/domTags.js";

/**
 * Sport details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function SportPage(state) {
    if (state.params.id === undefined)
        throw new LogError("Sport id must be defined");


    const id = state.params.id;
    const sport = await apiFetch(`/sports/${id}`);

    let {skip, limit} = getQuerySkipLimit(state.query, 0, 10);

    const activitiesData = await apiFetch(`/sports/${id}/activities?skip=${skip}&limit=${limit}`);

    activitiesData.skip = skip;
    activitiesData.limit = limit;

    /**
     * Updates a sport.
     * @param event form event
     */
    async function updateSport(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#newSportName").value;
        const description = form.querySelector("#newSportDescription").value;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/sports/" + id,
            {
                method: "PATCH",
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

    return Sport(
        state,
        {
            id: sport.id,
            name: sport.name,
            description: sport.description,
            activitiesData,
            onUpdateSubmit: updateSport
        }
    );
}

export default SportPage;
