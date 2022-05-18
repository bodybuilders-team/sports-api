import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError} from "../../js/utils.js";

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
    const {onRouteUpdated} = props;

    /**
     * Updates a route.
     * @param event form event
     */
    async function updateRoute(event) {
        event.preventDefault();
        const form = event.target;

        const startLocation = form.querySelector("#startLocation").value;
        const endLocation = form.querySelector("#endLocation").value;
        const distance = form.querySelector("#routeDistance").value;

        const token = getStoredUser().token

        const res = await fetch(
            "http://localhost:8888/api/routes/" + id,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance: Number(distance)})
            }
        );

        const json = await res.json();

        if (res.ok)
            onRouteUpdated(json);
        else
            await alertBoxWithError(state, form, json.extraInfo);
    }

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
                    {onSubmit: updateRoute},
                    label({for: "startLocation", class: "col-form-label"}, "New Start Location"),
                    input({
                        type: "text", id: "startLocation", name: "startLocation",
                        class: "form-control",
                        placeholder: "Enter new route start location"
                    }),

                    label({for: "endLocation", class: "col-form-label"}, "New End Location"),
                    input({
                        type: "text", id: "endLocation", name: "endLocation",
                        class: "form-control",
                        placeholder: "Enter new route end location"
                    }),

                    label({for: "distance", class: "col-form-label"}, "New Distance"),
                    input({
                        type: "text", id: "distance", name: "distance",
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