import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";

/**
 * EditRoute component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onUpdateSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function EditRoute(state, props) {
    return div(
        input(
            {
                type: "button",
                class: "btn btn-outline-primary inline w-100",
                value: "Edit",
                "data-bs-toggle": "collapse",
                "data-bs-target": "#editRoute"
            }
        ),
        br(),
        div(
            {class: "collapse w-75 center", id: "editRoute"},
            br(),
            div(
                {class: "card card-body"},
                h4("Edit Route"),
                form(
                    {onSubmit: props.onUpdateSubmit},
                    label({for: "newRouteStartLocation", class: "col-form-label"}, "New Start Location"),
                    input({
                        type: "text", id: "newRouteStartLocation", name: "newRouteStartLocation",
                        class: "form-control",
                        placeholder: "Enter new route start location"
                    }),

                    label({for: "newRouteEndLocation", class: "col-form-label"}, "New End Location"),
                    input({
                        type: "text", id: "newRouteEndLocation", name: "newRouteEndLocation",
                        class: "form-control",
                        placeholder: "Enter new route end location"
                    }),

                    label({for: "newRouteDistance", class: "col-form-label"}, "New Distance"),
                    input({
                        type: "text", id: "newRouteDistance", name: "newRouteDistance",
                        class: "form-control",
                        placeholder: "Enter new route distance"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Update")
                )
            )
        )
    );
}

export default EditRoute;