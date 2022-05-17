import {br, button, div, form, h4, input, label, p} from "../../js/dom/domTags.js";
import {alertBoxWithError} from "../../js/utils.js";

/**
 * CreateSport component.
 *
 * @param state - application state
 *
 * @return Promise<HTMLElement>
 */
async function CreateSport(state, props) {
    const {onSportCreated} = props;

    /**
     * Creates a sport.
     * @param event form event
     */
    async function createSport(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#sportName").value;
        const description = form.querySelector("#sportDescription").value;

        const token = window.localStorage.getItem("token");

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
            onSportCreated({id: json.id, name, description});
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
                        placeholder: "Enter sport name"
                    }),

                    label({for: "sportDescription", class: "col-form-label"}, "Description"),
                    input({
                        type: "text", id: "sportDescription", name: "sportDescription",
                        class: "form-control",
                        placeholder: "Enter sport description"
                    }),
                    br(),
                    button({type: "submit", class: "btn btn-primary w-100"}, "Create")
                )
            )
        ),
    );
}

export default CreateSport;