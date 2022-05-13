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
async function EditSport(state, props) {
    return div(
        input(
            {
                type: "button",
                class: "btn btn-outline-primary inline w-100",
                value: "Edit",
                "data-bs-toggle": "collapse",
                "data-bs-target": "#editSport"
            }
        ),
        br(),
        div(
            {class: "collapse w-75 center", id: "editSport"},
            br(),
            div(
                {class: "card card-body"},
                h4("Edit Sport"),
                form(
                    {onSubmit: props.onUpdateSubmit},
                    label({for: "newSportName", class: "col-form-label"}, "New Name"),
                    input({
                        type: "text", id: "newSportName", name: "newSportName",
                        class: "form-control",
                        placeholder: "Enter new sport name"
                    }),

                    label({for: "newSportDescription", class: "col-form-label"}, "New Description"),
                    input({
                        type: "text", id: "newSportDescription", name: "newSportDescription",
                        class: "form-control",
                        placeholder: "Enter new sport description"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Update")
                )
            )
        )
    );
}

export default EditSport;