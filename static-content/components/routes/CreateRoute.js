import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError} from "../../js/utils.js";

/**
 * CreateSport component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 *
 * @return Promise<HTMLElement>
 */
async function CreateRoute(state, props) {
    const {onRouteCreated} = props

    /**
     * Creates a route.
     * @param event form event
     */
    async function createRoute(event) {
        event.preventDefault();
        const form = event.target;

        const startLocation = form.querySelector("#routeStartLocation").value;
        const endLocation = form.querySelector("#routeEndLocation").value;
        const distance = form.querySelector("#routeDistance").value;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/routes/",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({startLocation, endLocation, distance: Number(distance)})
            }
        );

        const json = await res.json();

        if (res.ok)
            onRouteCreated({id: json.id, startLocation, endLocation, distance: Number(distance)});
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
                    label({for: "routeStartLocation", class: "col-form-label"}, "Start Location"),
                    input({
                        type: "text", id: "routeStartLocation", name: "routeStartLocation",
                        class: "form-control",
                        placeholder: "Enter route start location"
                    }),

                    label({for: "routeEndLocation", class: "col-form-label"}, "End Location"),
                    input({
                        type: "text", id: "routeEndLocation", name: "routeEndLocation",
                        class: "form-control",
                        placeholder: "Enter route end location"
                    }),

                    label({for: "routeDistance", class: "col-form-label"}, "Distance"),
                    input({
                        type: "text", id: "routeDistance", name: "routeDistance",
                        class: "form-control",
                        placeholder: "Enter route distance"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        )
    );
}

export default CreateRoute;