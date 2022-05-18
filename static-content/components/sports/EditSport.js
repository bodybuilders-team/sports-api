import {br, button, div, form, h4, input, label} from "../../js/dom/domTags.js";
import {alertBoxWithError, getStoredUser} from "../../js/utils.js";

/**
 * EditSport component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onSportUpdated - callback to be called when sport is updated
 *
 * @return Promise<HTMLElement>
 */
async function EditSport(state, props) {

    const {onSportUpdated} = props;

    /**
     * Updates a sport.
     * @param {Event} event form event
     */
    async function updateSport(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#name").value;
        const description = form.querySelector("#description").value;

        const token = getStoredUser().token

        const res = await fetch(
            "http://localhost:8888/api/sports/" + id,
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
                        placeholder: "Enter new sport name"
                    }),

                    label({for: "description", class: "col-form-label"}, "New Description"),
                    input({
                        type: "text", id: "description", name: "description",
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