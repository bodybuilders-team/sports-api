import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError, getStoredUser} from "../../js/utils.js";

/**
 * @typedef OnSportCreatedCallback
 * @param {CreatedSport} sport
 */

/**
 * CreateSport component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSportCreatedCallback} props.onSportCreated - on activity created callback
 *
 * @return Promise<HTMLElement>
 */
async function CreateSport(state, props) {

    const {onSportCreated} = props;

    /**
     * Creates a sport.
     * @param {Event} event form event
     */
    async function createSport(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#sportName").value;
        let description = form.querySelector("#sportDescription").value;

        if (description === "")
            description = null;

        if (name.length < 3 || name.length > 30) {
            await alertBoxWithError(state, form, "Name must be between 3 and 30 characters");
            return;
        }

        if (description !== null && description.length > 1000) {
            await alertBoxWithError(state, form, "Description must be less than 1000 characters");
            return;
        }

        const user = getStoredUser();
        if (user == null) {
            await alertBoxWithError(state, form, "You must be logged in to create a route");
            return;
        }

        const token = user.token;

        const res = await fetch(
            "http://localhost:8888/api/sports",
            {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({name, description})
            }
        );

        const json = await res.json();

        if (res.ok)
            onSportCreated({id: json.sid, name, description});
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
                {class: "card card-body bg-light"},
                h4("Create Sport"),
                form(
                    {onSubmit: createSport},
                    label({for: "sportName", class: "col-form-label"}, "Name"),
                    input({
                        type: "text", id: "sportName", name: "sportName",
                        class: "form-control",
                        placeholder: "Enter sport name", required: true, minlength: "3", maxlength: "30"
                    }),

                    label({for: "sportDescription", class: "col-form-label"}, "Description"),
                    input({
                        type: "text", id: "sportDescription", name: "sportDescription",
                        class: "form-control",
                        placeholder: "Enter sport description", minlength: "0", maxlength: "1000"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        )
    );
}

export default CreateSport;