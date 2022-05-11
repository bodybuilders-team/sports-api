import {a, br, button, div, form, h1, hr, input, label, option, select} from "../../js/dom/domTags.js";

/**
 * @typedef PropActivitiesProps
 * @property {?number} sid - Search sport id
 * @property {?number} rid - Search route id
 * @property {?string} orderBy - Search orderBy {"ascending" || "descending"}
 * @property {?string} date - Search date
 */

/**
 * @callback OnSubmitCallback
 * @param {Event} event - event
 */

/**
 * SearchActivitiesForm component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onSubmit - on Submit event callback
 * @param {?PropActivitiesProps=} props.activitiesProps - form activities props
 *
 * @return Promise<HTMLElement>
 */
async function SearchActivitiesForm(state, props) {
    const actProps = props.activitiesProps;

    function onSportInputChange(event) {
        event.preventDefault();
        const sportId = event.target.value;
        console.log("sportId", sportId);

    }

    return div(
        h1({class: "app_icon"}, "Activities"),
        div(
            {class: "card card-body w-50 center"},
            h1("Search for an Activity"),
            hr(),
            form(
                {onSubmit: props.onSubmit},
                div(
                    label({for: "sid", class: "col-form-label"}, "Sport Id"),
                    // input({
                    //     type: "number", min: "1", id: "sid",
                    //     class: "form-control",
                    //     value: (actProps != null && actProps.sid != null) ? actProps.sid.toString() : "",
                    //     required: true
                    // }),
                    div({class: "dropdown"},
                        button({
                            class: "btn btn-secondary dropdown-toggle", type: "button", id: "dropdownMenuButton",
                            "data-bs-toggle": "dropdown", "aria-expanded": "false",
                        }, "Select a sport..."),

                        div({class: "dropdown-menu", "aria-labelledby": "dropdownMenuButton"},
                            input({
                                type: "text", id: "sport", class: "form-control",
                                onInput: onSportInputChange
                            }),
                            a({class: "dropdown-item", "data-id": "5"}, "Lisboa-Porto"),
                            a({class: "dropdown-item", "data-id": "6"}, "Lisboa-Lisbon"),
                        )
                    ),
                    label({for: "rid", class: "form-label"}, "Route Id"),
                    input({
                        type: "number", min: "1", id: "rid",
                        value: (actProps != null && actProps.rid != null) ? actProps.rid.toString() : "",
                        class: "form-control"
                    }),

                    label({for: "orderBy", class: "form-label"}, "Order by"),
                    select(
                        {id: "orderBy", class: "form-control", required: true},
                        option({
                            value: "",
                            selected: (actProps == null) ? "" : undefined
                        }, "Select a order to display the results"),
                        option({
                                value: "ascending",
                                selected: (actProps != null && actProps.orderBy === "ascending") ? "" : undefined
                            },
                            "Ascending"),
                        option({
                            value: "descending",
                            selected: (actProps != null && actProps.orderBy === "descending") ? "" : undefined
                        }, "Descending")
                    ),

                    label({for: "date", class: "form-label"}, "Date"),
                    input({
                        type: "date", id: "date", class: "form-control",
                        value: (actProps != null && actProps.date != null) ? actProps.date : ""
                    })
                ),
                br(),
                button({type: "submit", class: "btn btn-primary"}, "Search")
            )
        )
    )
        ;
}

export default SearchActivitiesForm;