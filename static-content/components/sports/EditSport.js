import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError, getStoredUser} from "../../js/utils.js";
import {API_BASE_URL} from "../../js/config.js";

/**
 * EditSport component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - route id
 * @param {onUpdateCallback} props.onSportUpdated - callback to be called when sport is updated
 *
 * @return Promise<HTMLElement>
 */
async function EditSport(state, props) {

    const {id, onSportUpdated} = props;

    /**
     * Updates a sport.
     * @param {Event} event form event
     */
    async function updateSport(event) {
        event.preventDefault();
        const form = event.target;

        let name = form.querySelector("#name").value;
        let description = form.querySelector("#description").value;

        if (name === "")
            name = null;

        if (description === "")
            description = null;

        if (name == null && description == null) {
            await alertBoxWithError(state, form, "Please fill atleast one of the fields");
            return;
        }

        if (name !== null && (name.length < 3 || name.length > 30)) {
            await alertBoxWithError(state, form, "Name must be between 3 and 30 characters");
            return;
        }

        if (description !== null && description.length > 1000) {
            await alertBoxWithError(state, form, "Description must be less than 1000 characters");
            return;
        }

        const token = getStoredUser().token;

        const res = await fetch(
            `${API_BASE_URL}/sports/${id}`,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({name, description})
            }
        );

        const json = await res.json();

        if (res.ok)
            onSportUpdated(json);
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
                    {onSubmit: updateSport},
                    label({for: "name", class: "col-form-label"}, "New Name"),
                    input({
                        type: "text", id: "name", name: "name",
                        class: "form-control",
                        placeholder: "Enter new sport name", minlength: "3", maxlength: "30"
                    }),

                    label({for: "description", class: "col-form-label"}, "New Description"),
                    input({
                        type: "text", id: "description", name: "description",
                        class: "form-control",
                        placeholder: "Enter new sport description", minlength: "0", maxlength: "1000"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Update")
                )
            )
        )
    );
}

export default EditSport;