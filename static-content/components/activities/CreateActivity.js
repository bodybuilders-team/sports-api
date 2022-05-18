import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError, createRef, getStoredUser} from "../../js/utils.js";
import SportsDropdown from "../sports/SportsDropdown.js";
import RoutesDropdown from "../routes/RoutesDropdown.js";

/**
 * CreateActivity component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onActivityCreated - on activity created callback
 *
 * @return Promise<HTMLElement>
 */
async function CreateActivity(state, props) {
    const {onActivityCreated} = props;

    const sportsIdInputRef = createRef()
    const invalidSportFeedbackRef = createRef()
    const routeIdInputRef = createRef()

    /**
     * Creates an activity.
     * @param event form event
     */
    async function createActivity(event) {
        event.preventDefault();
        const form = event.target;

        const sid = form.querySelector("#sid").value;
        const date = form.querySelector("#date").value;
        const duration = form.querySelector("#duration").value;
        let rid = form.querySelector("#rid").value;

        if (rid === "")
            rid = null;

        if (date === "") {
            await alertBoxWithError(state, form, "Please enter a date");
            return;
        }

        const invalidSportFeedback = await invalidSportFeedbackRef
        if (sid === "") {
            invalidSportFeedback.style.display = "block";
            return
        }

        invalidSportFeedback.style.display = "none";

        const token = getStoredUser().token

        const res = await fetch(
            "http://localhost:8888/api/activities/",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({sid, date, duration, rid})
            }
        );

        const json = await res.json();

        if (res.ok)
            onActivityCreated({id: json.aid, sid, date, duration, rid});
        else
            await alertBoxWithError(state, form, json.extraInfo);
    }

    async function onSportChange(id) {
        const sportIdInput = await sportsIdInputRef
        sportIdInput.value = id

        const invalidSportFeedback = await invalidSportFeedbackRef
        invalidSportFeedback.style.display = "none";
    }

    async function onRouteChange(id) {
        const routeIdInput = await routeIdInputRef
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

                    label({for: "date", class: "col-form-label"}, "Date"),
                    input({
                        type: "date", id: "date", name: "date",
                        class: "form-control",
                        placeholder: "Enter activity date"
                    }),

                    label({for: "duration", class: "col-form-label"}, "Duration"),
                    input({
                        type: "text", id: "duration", name: "duration",
                        class: "form-control",
                        placeholder: "Enter activity duration"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        ),
    )
        ;
}

export default CreateActivity;