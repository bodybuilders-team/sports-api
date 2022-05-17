import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError} from "../../js/utils.js";

/**
 * EditSport component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 *
 * @return Promise<HTMLElement>
 */
async function EditActivity(state, props) {
    const {id, onActivityUpdated} = props;

    async function updateActivity(event) {

        event.preventDefault();
        const form = event.target;

        const sid = form.querySelector("#sid").value;
        const date = form.querySelector("#date").value;
        const duration = form.querySelector("#duration").value;
        const rid = form.querySelector("#aid").value;

        const token = window.localStorage.getItem("token");

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
            onActivityUpdated()
        else
            await alertBoxWithError(state, form, json);
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

                    label({for: "newActivitySport", class: "col-form-label"}, "New Sport"),
                    input({
                        type: "text", id: "newActivitySport", name: "newActivitySport",
                        class: "form-control",
                        placeholder: "Enter new activity sport"
                    }),

                    label({for: "newActivityDate", class: "col-form-label"}, "New Date"),
                    input({
                        type: "date", id: "newActivityDate", name: "newActivityDate",
                        class: "form-control",
                        placeholder: "Enter new activity date"
                    }),

                    label({for: "newActivityDuration", class: "col-form-label"}, "New Duration"),
                    input({
                        type: "text", id: "newActivityDuration", name: "newActivityDuration",
                        class: "form-control",
                        placeholder: "Enter new activity duration"
                    }),

                    label({for: "newActivityRoute", class: "col-form-label"}, "New Route"),
                    input({
                        type: "text", id: "newActivityDuration", name: "newActivityDuration",
                        class: "form-control",
                        placeholder: "Enter new activity route"
                    }),

                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Update")
                )
            )
        )
    );
}

export default EditActivity;