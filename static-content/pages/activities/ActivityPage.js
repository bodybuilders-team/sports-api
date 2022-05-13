import apiFetch from "../../js/apiFetch.js";
import Activity from "../../components/activities/Activity.js";
import {LogError} from "../../js/errorUtils.js";
import {br, div} from "../../js/dom/domTags.js";
import {reloadHash} from "../../js/utils.js";

/**
 * Activity details page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function ActivityPage(state) {
    if (state.params.id === undefined)
        throw new LogError("User id must be defined");

    const id = parseInt(state.params.id);
    if (id == null)
        throw new LogError("User id");

    const activity = await apiFetch(`/activities/${id}`);

    const sport = await apiFetch(`/sports/${activity.sid}`);

    const route = activity.rid !== undefined
        ? await apiFetch(`/routes/${activity.rid}`)
        : null;

    /**
     * Updates an activity.
     * @param event form event
     */
    async function updateActivity(event) {
        event.preventDefault();
        const form = event.target;

        const sport = form.querySelector("#newActivitySport").value;
        const date = form.querySelector("#newActivityDate").value;
        const duration = form.querySelector("#newActivityDuration").value;
        const route = form.querySelector("#newActivityRoute").value;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/activities/" + id,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({sport, date, duration, route})
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

    return Activity(
        state,
        {
            id,
            date: activity.date,
            duration: activity.duration,
            sport,
            route,
            onUpdateSubmit: updateActivity
        }
    );
}

export default ActivityPage;
