import {br, button, div, form, h4, input} from "../../js/dom/domTags.js";
import AlertBox from "../AlertBox.js";
import {alertBoxWithError, createRef} from "../../js/utils.js";

/**
 * DeleteActivity component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 *
 * @return Promise<HTMLElement>
 */
async function DeleteActivity(state, props) {
    const {id, onActivityDeleted} = props;

    let deleting = false
    const deleteButtonRef = createRef()

    /**
     * Deletes an activity.
     * @param event form event
     */
    async function deleteActivity(event) {
        event.preventDefault();
        if (deleting) return;

        deleting = true
        const deleteButton = await deleteButtonRef
        deleteButton.disabled = true

        const form = event.target;

        const token = window.localStorage.getItem("token");

        const res = await fetch(
            "http://localhost:8888/api/activities/" + id,
            {
                method: "DELETE",
                headers: {'Authorization': `Bearer ${token}`}
            }
        );

        const json = await res.json();

        if (res.ok)
            onActivityDeleted()
        else
            await alertBoxWithError(state, form, json);
    }

    return div(
        input(
            {
                type: "button",
                class: "btn btn-outline-danger inline w-100",
                value: "Delete",
                "data-bs-toggle": "collapse",
                "data-bs-target": "#deleteActivity"
            }
        ),
        br(),
        div(
            {class: "collapse w-75 center", id: "deleteActivity"},
            br(),
            div(
                {class: "card card-body"},
                h4("Delete Activity"),
                form(
                    {onSubmit: deleteActivity},

                    AlertBox(
                        state,
                        {
                            alertLevel: "danger",
                            alertMessage: "Are you sure you want to delete this activity?"
                        }
                    ),

                    br(),
                    button({type: "submit", class: "btn btn-danger w-100", ref: deleteButtonRef}, "Delete")
                )
            )
        )
    );
}

export default DeleteActivity;