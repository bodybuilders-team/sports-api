import {br, button, div, form, h1, hr, input, label} from "../../js/dom/domTags.js";
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
async function SearchUsersForm(state, props) {
    const {onSubmit} = props;
    const sportsIdInputRef = createRef()
    const invalidSportFeedbackRef = createRef()
    const routeIdInputRef = createRef()


    async function onFormSubmit(event) {
        event.preventDefault();

        const form = event.target;

        const sid = form.querySelector("#sid").value;
        let rid = form.querySelector("#rid").value;

        if (rid === "")
            rid = null;

        const invalidSportFeedback = await invalidSportFeedbackRef
        if (sid === "") {
            invalidSportFeedback.style.display = "block";
            return
        }

        invalidSportFeedback.style.display = "none";

        onSubmit({sid, rid});
    }


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


    return div(
        {class: "card card-body w-50 center bg-light"},
        h1("Search for Users rankings"),
        hr(),
        form({class: "neeeds-validation", onSubmit: onFormSubmit},
            div(
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
                    div({class: "invalid-feedback", ref: invalidSportFeedbackRef}, "Please select a sport")
                ),

                div(
                    label({for: "rid", class: "form-label"}, "Route"),
                    input({
                        type: "hidden",
                        id: "rid",
                        ref: routeIdInputRef
                    }),
                    RoutesDropdown(state, {
                        onChange: onRouteChange
                    }),
                )
            ),
            br(),
            button({type: "submit", class: "btn btn-primary w-100"}, "Search")
        )
    )
}

export default SearchUsersForm;
