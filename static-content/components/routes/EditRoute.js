import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError, getStoredUser} from "../../js/utils.js";
import {API_BASE_URL} from "../../js/config.js";

/**
 * EditRoute component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - route id
 * @param {onUpdateCallback} props.onRouteUpdated - callback to be called when route is updated
 *
 * @return Promise<HTMLElement>
 */
async function EditRoute(state, props) {

    const {id, onRouteUpdated} = props;

    /**
     * Updates a route.
     * @param {Event} event form event
     */
    async function updateRoute(event) {
        event.preventDefault();
        const form = event.target;

        let startLocation = form.querySelector("#startLocation").value;
        let endLocation = form.querySelector("#endLocation").value;
        let distance = form.querySelector("#distance").value;

        if (startLocation === "")
            startLocation = null;

        if (endLocation === "")
            endLocation = null;

        if (distance === "")
            distance = null;

        if (startLocation == null && endLocation == null && distance == null) {
            await alertBoxWithError(state, form, "Please fill atleast one of the fields");
            return;
        }

        if (startLocation !== null && (startLocation.length < 3 || startLocation.length > 250)) {
            await alertBoxWithError(state, form, "Start location must be between 3 and 250 characters");
            return;
        }

        if (endLocation !== null && (endLocation.length < 3 || endLocation.length > 250)) {
            await alertBoxWithError(state, form, "End location must be between 3 and 250 characters");
            return;
        }

        const user = getStoredUser();
        if (user == null) {
            await alertBoxWithError(state, form, "You must be logged in to create a route");
            return;
        }

        const token = user.token;

        const res = await fetch(
            `${API_BASE_URL}/routes/${id}`,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance})
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
                        class: "form-control", minlength: "3", maxlength: "250",
                        placeholder: "Enter new route start location"
                    }),

                    label({for: "endLocation", class: "col-form-label"}, "New End Location"),
                    input({
                        type: "text", id: "endLocation", name: "endLocation",
                        class: "form-control", minlength: "3", maxlength: "250",
                        placeholder: "Enter new route end location"
                    }),

                    label({for: "distance", class: "col-form-label"}, "New Distance"),
                    input({
                        type: "number", step: "0.01", min: "0", id: "distance", name: "distance",
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