import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError, createRef, getStoredUser} from "../../js/utils.js";
import SportsDropdown from "../sports/SportsDropdown.js";
import RoutesDropdown from "../routes/RoutesDropdown.js";

/**
 * EditActivity component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {onUpdateCallback} props.onActivityUpdated - callback to be called when activity is updated
 *
 * @return Promise<HTMLElement>
 */
async function EditActivity(state, props) {

    const {id, onActivityUpdated} = props;

    const sportsIdInputRef = createRef();
    const invalidSportFeedbackRef = createRef();
    const routeIdInputRef = createRef();

    /**
     * Updates an activity.
     * @param {Event} event form event
     */
    async function updateActivity(event) {
        event.preventDefault();
        const form = event.target;

        let sid = form.querySelector("#sid").value;
        let date = form.querySelector("#date").value;
        let duration = form.querySelector("#duration").value;
        let rid = form.querySelector("#rid").value;

        if (sid === "")
            sid = null;

        if (duration === "")
            duration = null;

        if (rid === "")
            rid = null;

        if (date === "")
            date = null;

        if (sid == null && rid == null && date == null && duration == null) {
            await alertBoxWithError(state, form, "Please fill atleast one of the fields");
            return;
        }

        const user = getStoredUser();
        if (user == null) {
            await alertBoxWithError(state, form, "You must be logged in to create a route");
            return;
        }

        const token = user.token;

        const res = await fetch(
            "http://localhost:8888/api/activities/" + id,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({sid, date, duration, rid})
            }
        );

        const json = await res.json();

        if (res.ok)
            onActivityUpdated(json);
        else
            await alertBoxWithError(state, form, json.extraInfo);
    }

    /**
     * Callback for when a sport is selected.
     * @param id sport id
     */
    async function onSportChange(id) {
        const sportIdInput = await sportsIdInputRef;
        sportIdInput.value = id;

        const invalidSportFeedback = await invalidSportFeedbackRef;
        invalidSportFeedback.style.display = "none";
    }

    /**
     * Callback for when a route is selected.
     * @param id route id
     */
    async function onRouteChange(id) {
        const routeIdInput = await routeIdInputRef;
        routeIdInput.value = id;
    }

    return div(
        input(
            {
                type: "button",
                class: "btn btn-outline-primary inline w-100",
                value: "Edit",
                "data-bs-toggle": "collapse",
                "data-bs-target": "#editActivity"
            }
        ),
        br(),
        div(
            {class: "collapse w-75 center", id: "editActivity"},
            br(),
            div(
                {class: "card card-body"},
                h4("Edit Activity"),
                form(
                    {onSubmit: updateActivity},
                    div(
                        label({for: "sid", class: "col-form-label"}, "Sport"),
                        input({
                            type: "hidden",
                            id: "sid",
                            ref: sportsIdInputRef
                        }),
                        SportsDropdown(state, {
                            onChange: onSportChange
                        }),
                        div({class: "invalid-feedback", ref: invalidSportFeedbackRef}, "Please select a sport")
                    ),

                    div(
                        label({for: "rid", class: "form-label"}, "Route"),
                        input({
                            type: "hidden",
                            id: "rid",
                            ref: routeIdInputRef
                        }),
                        RoutesDropdown(state, {
                            onChange: onRouteChange
                        }),
                    ),

                    label({for: "date", class: "col-form-label"}, "New Date"),
                    input({
                        type: "date", id: "date", name: "date",
                        class: "form-control",
                        placeholder: "Enter new activity date"
                    }),

                    label({for: "duration", class: "col-form-label"}, "New Duration"),
                    input({
                        type: "time", step: "0.001", id: "duration", name: "duration",
                        class: "form-control",
                        placeholder: "Enter new activity duration"
                    }),

                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Update")
                )
            )
        )
    );
}

export default EditActivity;