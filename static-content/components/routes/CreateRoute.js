import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError, getStoredUser} from "../../js/utils.js";

/**
 * @typedef OnRouteCreatedCallback
 * @param {CreatedRoute} route
 */

/**
 * CreateRoute component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {OnRouteCreatedCallback} props.onRouteCreated - callback to be called when route is created
 *
 * @return Promise<HTMLElement>
 */
async function CreateRoute(state, props) {

    const {onRouteCreated} = props

    /**
     * Creates a route.
     * @param {Event} event form event
     */
    async function createRoute(event) {
        event.preventDefault();
        const form = event.target;

        const startLocation = form.querySelector("#startLocation").value;
        const endLocation = form.querySelector("#endLocation").value;
        const distance = form.querySelector("#distance").value;

        if (startLocation.length < 3 || startLocation.length > 250) {
            await alertBoxWithError(state, form, "Start location must be between 3 and 250 characters");
            return;
        }

        if (endLocation.length < 3 || endLocation.length > 250) {
            await alertBoxWithError(state, form, "End location must be between 3 and 250 characters");
            return;
        }

        if (distance === "") {
            await alertBoxWithError(state, form, "Please enter a distance");
            return;
        }

        const user = getStoredUser();
        if (user == null) {
            await alertBoxWithError(state, form, "You must be logged in to create a route");
            return;
        }

        const token = user.token;

        const res = await fetch(
            "http://localhost:8888/api/routes/",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance})
            }
        );

        const json = await res.json();

        if (res.ok)
            onRouteCreated({id: json.rid, startLocation, endLocation, distance});
        else
            await alertBoxWithError(state, form, json.extraInfo);
    }

    return div(
        p(
            {align: "center"},
            input(
                {
                    type: "button",
                    class: "btn btn-primary inline w-50",
                    value: "Create Route",
                    "data-bs-toggle": "collapse",
                    "data-bs-target": "#createRoute"
                }
            )
        ),
        div(
            {class: "collapse w-50 center", id: "createRoute"},
            br(),
            div(
                {class: "card card-body bg-light"},
                h4("Create Route"),
                form(
                    {onSubmit: createRoute},
                    label({for: "startLocation", class: "col-form-label"}, "Start Location"),
                    input({
                        type: "text", id: "startLocation", name: "startLocation",
                        class: "form-control", minlength: "3", maxlength: "250",
                        placeholder: "Enter route start location", required: true
                    }),

                    label({for: "endLocation", class: "col-form-label"}, "End Location"),
                    input({
                        type: "text", id: "endLocation", name: "endLocation",
                        class: "form-control", minlength: "3", maxlength: "250",
                        placeholder: "Enter route end location", required: true
                    }),

                    label({for: "distance", class: "col-form-label"}, "Distance"),
                    input({
                        type: "number", step: "0.01", min: "0", id: "distance", name: "distance",
                        class: "form-control",
                        placeholder: "Enter route distance", required: true
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        )
    );
}

export default CreateRoute;