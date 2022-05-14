import {a, br, div, h1, h3, h5} from "../../js/dom/domTags.js";
import EditActivity from "./EditActivity.js";
import DeleteActivity from "./DeleteActivity.js";

/**
 * @typedef PropRoute
 * @property {string} id activity route id
 */

/**
 * Activity details component.
 *
 * @param state - application state
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {string} props.date - activity date
 * @param {string} props.duration - activity duration
 * @param {Object} props.sport - activity sport
 * @param {number} props.sport.id - activity sport id
 * @param {string} props.sport.name - activity sport name
 * @param {?PropRoute=} props.route - activity route
 * @param {OnSubmitCallback} props.onUpdateSubmit - on Submit event callback
 * @param {OnSubmitCallback} props.onDeleteSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function Activity(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Activity ${props.id}`),
        div(
            {class: "card user_card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "activitySport"}, "Sport: ", a({href: `#sports/${props.sport.id}`}, props.sport.name)),
                h3({id: "activityDate"}, "Date: ", props.date),
                h3({id: "activityDuration"}, "Duration: ", props.duration),
                props.route != null
                    ? h5({id: "activityRoute"}, "Route: ", a({href: `#routes/${props.route.id}`}, props.route.id.toString()))
                    : undefined,
                br(),
                EditActivity(state, props),
                br(),
                DeleteActivity(state, props)
            )
        )
    );
}

export default Activity;
