import {br, button, div, form, h4, input} from "../../js/dom/domTags.js";
import AlertBox from "../AlertBox.js";
import {alertBoxWithError, createRef, getStoredUser} from "../../js/utils.js";
import {API_BASE_URL} from "../../js/config.js";

/**
 * @typedef OnActivityDeletedCallback
 */

/**
 * DeleteActivity component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {OnActivityDeletedCallback} props.onActivityCreated - on activity deleted callback
 *
 * @return Promise<HTMLElement>
 */
async function DeleteActivity(state, props) {

    const {id, onActivityDeleted} = props;

    let deleting = false;
    const deleteButtonRef = createRef();

    /**
     * Deletes an activity.
     * @param {Event} event form event
     */
    async function deleteActivity(event) {
        event.preventDefault();
        if (deleting) return;

        deleting = true;
        const deleteButton = await deleteButtonRef;
        deleteButton.disabled = true;

        const form = event.target;

        const token = getStoredUser().token;

        const res = await fetch(
            `${API_BASE_URL}/activities/${id}`,
            {
                method: "DELETE",
                headers: {'Authorization': `Bearer ${token}`}
            }
        );

        const json = await res.json();

        if (res.ok)
            onActivityDeleted();
        else
            await alertBoxWithError(state, form, json.extraInfo);
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