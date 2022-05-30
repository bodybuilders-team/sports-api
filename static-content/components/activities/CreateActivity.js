import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError, createRef, getStoredUser} from "../../js/utils.js";
import SportsDropdown from "../sports/SportsDropdown.js";
import RoutesDropdown from "../routes/RoutesDropdown.js";
import {API_BASE_URL} from "../../js/config.js";

/**
 * @typedef OnActivityCreatedCallback
 * @param {Activity} activity
 */

/**
 * CreateActivity component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {OnActivityCreatedCallback} props.onActivityCreated - callback to be called when activity is created
 *
 * @return Promise<HTMLElement>
 */
async function CreateActivity(state, props) {

    const {onActivityCreated} = props;

    const sportsIdInputRef = createRef();
    const invalidSportFeedbackRef = createRef();
    const routeIdInputRef = createRef();

    /**
     * Creates an activity.
     * @param {Event} event form event
     */
    async function createActivity(event) {
        event.preventDefault();
        const form = event.target;

        const sid = form.querySelector("#sid").value;
        const date = form.querySelector("#date").value;
        const duration = form.querySelector("#duration").value;
        let rid = form.querySelector("#rid").value;

        const invalidSportFeedback = await invalidSportFeedbackRef
        if (sid === "") {
            invalidSportFeedback.style.display = "block";
            return;
        }

        invalidSportFeedback.style.display = "none";

        if (rid === "")
            rid = null;

        if (duration === "") {
            await alertBoxWithError(state, form, "Please enter a duration");
            return;
        }

        if (date === "") {
            await alertBoxWithError(state, form, "Please enter a date");
            return;
        }

        const user = getStoredUser();
        if (user == null) {
            await alertBoxWithError(state, form, "You must be logged in to create a route");
            return;
        }

        const token = user.token;

        const res = await fetch(
            `${API_BASE_URL}/activities/`,
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({sid, date, duration, rid})
            }
        );

        /**
         * @type {Object}
         * @property {number} aid activity id
         */
        const json = await res.json();

        if (res.ok)
            onActivityCreated({id: json.aid, sid, date, duration, rid});
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
        p(
            {align: "center"},
            input(
                {
                    type: "button",
                    class: "btn btn-primary inline w-50",
                    value: "Create Activity",
                    "data-bs-toggle": "collapse",
                    "data-bs-target": "#createActivity"
                }
            )
        ),
        div(
            {class: "collapse w-50 center", id: "createActivity"},
            br(),
            div(
                {class: "card card-body bg-light"},
                h4("Create Activity"),
                form(
                    {onSubmit: createActivity},
                    div(
                        label({for: "sid", class: "col-form-label"}, "Sport"),
                        input({
                            type: "hidden",
                            id: "sid",
                            ref: sportsIdInputRef
                        }),
                        SportsDropdown(state, {onChange: onSportChange}),
                        div({class: "invalid-feedback", ref: invalidSportFeedbackRef}, "Please select a sport")
                    ),

                    div(
                        label({for: "rid", class: "form-label"}, "Route"),
                        input({
                            type: "hidden",
                            id: "rid",
                            ref: routeIdInputRef
                        }),
                        RoutesDropdown(state, {onChange: onRouteChange}),
                    ),

                    label({for: "date", class: "col-form-label"}, "Date"),
                    input({
                        type: "date", id: "date", name: "date",
                        class: "form-control",
                        placeholder: "Enter activity date", required: true
                    }),

                    label({for: "duration", class: "col-form-label"}, "Duration"),
                    input({
                        type: "time", step: "0.001", id: "duration", name: "duration",
                        class: "form-control",
                        placeholder: "Enter activity duration", required: true
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        )
    );
}

export default CreateActivity;