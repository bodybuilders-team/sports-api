import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";

/**
 * CreateSport component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onCreateSubmint - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function CreateSport(state, props) {
    return div(
        p(
            {align: "center"},
            input(
                {
                    type: "button",
                    class: "btn btn-primary inline w-50",
                    value: "Create Sport",
                    "data-bs-toggle": "collapse",
                    "data-bs-target": "#createSport"
                }
            )
        ),
        div(
            {class: "collapse w-50 center", id: "createSport"},
            br(),
            div(
                {class: "card card-body"},
                h4("Create Sport"),
                form(
                    {onSubmit: props.onCreateSubmint},
                    label({for: "sportName", class: "col-form-label"}, "Name"),
                    input({
                        type: "text", id: "sportName", name: "sportName",
                        class: "form-control",
                        placeholder: "Enter sport name"
                    }),

                    label({for: "sportDescription", class: "col-form-label"}, "Description"),
                    input({
                        type: "text", id: "sportDescription", name: "sportDescription",
                        class: "form-control",
                        placeholder: "Enter sport description"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        ),
    );
}

export default CreateSport;