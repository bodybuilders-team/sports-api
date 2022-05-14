import {br, button, div, form, h4, input} from "../../js/dom/domTags.js";
import AlertBox from "../AlertBox.js";

/**
 * DeleteActivity component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onDeleteSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function DeleteActivity(state, props) {
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
                    {onSubmit: props.onDeleteSubmit},

                    AlertBox(
                        state,
                        {
                            alertLevel: "danger",
                            alertMessage: "Are you sure you want to delete this activity?"
                        }
                    ),

                    br(),
                    button({type: "submit", class: "btn btn-danger w-100"}, "Delete")
                )
            )
        )
    );
}

export default DeleteActivity;