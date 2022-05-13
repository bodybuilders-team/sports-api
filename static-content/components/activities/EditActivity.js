import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";

/**
 * EditSport component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onUpdateSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function EditActivity(state, props) {
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
                    {onSubmit: props.onUpdateSubmit},

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