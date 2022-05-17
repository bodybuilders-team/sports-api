import {br, button, div, form, h1, hr, input, label, option, select} from "../../js/dom/domTags.js";
import {createRef} from "../../js/utils.js";
import SportsDropdown from "../sports/SportsDropdown.js";
import RoutesDropdown from "../routes/RoutesDropdown.js";

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
 *
 * @return Promise<HTMLElement>
 */
async function SearchActivitiesForm(state, props) {
    const {onSubmit} = props;
    const invalidSportFeedbackRef = createRef()

    async function onFormSubmit(event) {
        event.preventDefault();
        const form = event.target;

        const sid = form.querySelector("#sid").value;
        const orderBy = form.querySelector("#orderBy").value;
        let rid = form.querySelector("#rid").value;
        let date = form.querySelector("#date").value;

        if (date === "")
            date = null

        if (rid === "")
            rid = null

        const invalidSportFeedback = await invalidSportFeedbackRef
        if (sid === "") {
            invalidSportFeedback.style.display = "block";
            return
        }

        invalidSportFeedback.style.display = "none";

        onSubmit({sid, orderBy, rid, date});
    }

    const sportsIdInputRef = createRef()
    const routeIdInputRef = createRef()

    async function onSportChange(id) {
        const sportIdInput = await sportsIdInputRef
        sportIdInput.value = id

        const invalidSportFeedback = await invalidSportFeedbackRef
        invalidSportFeedback.style.display = "none";
    }

    async function onRouteChange(id) {
        const routeIdInput = await routeIdInputRef
        routeIdInput.value = id;
    }

    async function checkFormValidity(event) {
        const form = event.target.form;
        const invalidSportFeedback = await invalidSportFeedbackRef

        const sid = form.querySelector("#sid").value;

        if (sid === "") {
            invalidSportFeedback.style.display = "block";
            return
        }

        invalidSportFeedback.style.display = "none";
    }

    return div(
        {class: "card card-body w-50 center bg-light"},
        h1("Search for an Activity"),
        hr(),
        form(
            {onSubmit: onFormSubmit},
            div(
                label({for: "sid", class: "col-form-label"}, "Sport"),
                input({
                    type: "hidden",
                    id: "sid",
                    ref: sportsIdInputRef
                }),
                SportsDropdown(state, {
                    onChange: onSportChange
                }),
                div({class: "invalid-feedback", ref: invalidSportFeedbackRef}, "Please select a sport"),
            ),
            label({for: "rid", class: "form-label"}, "Route"),
            input({
                type: "hidden",
                id: "rid",
                ref: routeIdInputRef
            }),
            RoutesDropdown(state, {
                onChange: onRouteChange
            }),

            label({for: "orderBy", class: "form-label"}, "Order by"),
            select(
                {id: "orderBy", class: "form-control", required: true, onInvalid: checkFormValidity},
                option({
                    value: "",
                    selected: ""
                }, "Select a order to display the results"),
                option({
                        value: "ascending",
                    },
                    "Ascending"),
                option({
                    value: "descending",
                }, "Descending")
            ),

            label({for: "date", class: "form-label"}, "Date"),
            input({
                type: "date", id: "date", class: "form-control",
            }),
            br(),
            button({type: "submit", class: "btn btn-primary w-100"}, "Search")
        ),
    )
}

export default SearchActivitiesForm;