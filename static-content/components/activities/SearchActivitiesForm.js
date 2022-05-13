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
 * @param {} props.sports - form sports
 * @param {} props.routes - form routes
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
        {class: "card card-body w-50 center"},
        h1("Search for an Activity"),
        hr(),
        form(
            {onSubmit: props.onSubmit},
            div(
                label({for: "sid", class: "col-form-label"}, "Sport"),
                div({class: "dropdown"},
                    button(
                        {
                            class: "btn btn-secondary dropdown-toggle w-100",
                            type: "button",
                            id: "dropdownMenuButton",
                            "data-bs-toggle": "dropdown",
                            "aria-expanded": "false",
                        },
                        "Select a sport"
                    ),

                    div(
                        {class: "dropdown-menu w-100", "aria-labelledby": "dropdownMenuButton"},
                        input({
                            type: "text", id: "sid", class: "form-control",
                            onInput: onSportInputChange
                        }),
                        ...props.sports.sports.map(sport =>
                            a({class: "dropdown-item"}, sport.name)
                        )
                    )
                ),
                label({for: "rid", class: "form-label"}, "Route"),
                div({class: "dropdown"},
                    button(
                        {
                            class: "btn btn-secondary dropdown-toggle w-100",
                            type: "button",
                            id: "dropdownMenuButton",
                            "data-bs-toggle": "dropdown",
                            "aria-expanded": "false",
                        },
                        "Select a route"
                    ),

                    div(
                        {class: "dropdown-menu w-100", "aria-labelledby": "dropdownMenuButton"},
                        input({
                            type: "text", id: "sid", class: "form-control",
                            onInput: onSportInputChange
                        }),
                        ...props.routes.routes.map(route =>
                            a({class: "dropdown-item"}, `${route.startLocation} - ${route.endLocation}`)
                        )
                    )
                ),

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
            button({type: "submit", class: "btn btn-primary w-100"}, "Search")
        )
    );
}

export default SearchActivitiesForm;