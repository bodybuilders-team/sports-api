import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError} from "../../js/utils.js";

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
        const rid = form.querySelector("#aid").value;

        const token = window.localStorage.getItem("token");

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
            await alertBoxWithError(state, form, json);
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
                    label({for: "activitySport", class: "col-form-label"}, "Sport"),
                    input({
                        type: "text", id: "activitySport", name: "activitySport",
                        class: "form-control",
                        placeholder: "Enter activity sport"
                    }),

                    label({for: "activityDate", class: "col-form-label"}, "Date"),
                    input({
                        type: "date", id: "activityDate", name: "activityDate",
                        class: "form-control",
                        placeholder: "Enter activity date"
                    }),

                    label({for: "activityDuration", class: "col-form-label"}, "Duration"),
                    input({
                        type: "text", id: "activityDuration", name: "activityDuration",
                        class: "form-control",
                        placeholder: "Enter activity duration"
                    }),

                    label({for: "activityRoute", class: "col-form-label"}, "Route"),
                    input({
                        type: "text", id: "activityRoute", name: "activityRoute",
                        class: "form-control",
                        placeholder: "Enter activity route"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        ),
    );
}

export default CreateActivity;