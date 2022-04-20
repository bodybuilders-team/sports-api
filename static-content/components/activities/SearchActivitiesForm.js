import {button, div, form, h1, hr, input, label, option, select} from "../../js/dom/domTags.js";
import Activities from "./Activities.js";

/**
 * SearchActivitiesForm component.
 * @param state application state
 * @param props component properties
 * @returns activities component
 */
async function SearchActivitiesForm(state, props) {
    if (props == null)
        throw new Error("SearchActivitiesForm props must not be null");

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
                    input({
                        type: "number", min: "0", id: "sid",
                        class: "form-control",
                        value: (props.activitiesProps != null) ? props.activitiesProps.sid : "",
                        required: true
                    }),

                    label({for: "rid", class: "form-label"}, "Route Id"),
                    input({
                        type: "number", min: "0", id: "rid",
                        value: (props.activitiesProps != null) ? props.activitiesProps.rid : "",
                        class: "form-control"
                    }),

                    label({for: "orderBy", class: "form-label"}, "Order by"),
                    select(
                        {id: "orderBy", class: "form-control", required: true},
                        option({
                            value: "",
                            selected: (props.activitiesProps == null)
                        }, "Select a order to display the results"),
                        option({
                                value: "ascending",
                                selected: (props.activitiesProps != null && props.activitiesProps.orderBy === "ascending")
                            },
                            "Ascending"),
                        option({
                            value: "descending",
                            selected: (props.activitiesProps != null && props.activitiesProps.orderBy === "descending")
                        }, "Descending")
                    ),

                    label({for: "date", class: "form-label"}, "Date"),
                    input({
                        type: "date", id: "date", class: "form-control",
                        value: (props.activitiesProps != null) ? props.activitiesProps.date : ""
                    })
                ),
                button({type: "submit", class: "btn btn-primary"}, "Search")
            )
        )
    );
}

export default SearchActivitiesForm;