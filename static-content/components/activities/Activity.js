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
 *
 * @return Promise<HTMLElement>
 */
async function Activity(state, props) {
    const {id, date, duration, sport, route, onActivityUpdated, onActivityDeleted} = props;

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Activity ${id}`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "activitySport"}, "Sport: ", a({href: `#sports/${sport.id}`}, sport.name)),
                h3({id: "activityDate"}, "Date: ", date),
                h3({id: "activityDuration"}, "Duration: ", duration),
                route != null
                    ? h5({id: "activityRoute"}, "Route: ", a({href: `#routes/${route.id}`}, route.id.toString()))
                    : undefined,
                br(),
                EditActivity(state, {id, onActivityUpdated}),
                br(),
                DeleteActivity(state, {id, onActivityDeleted})
            )
        )
    );
}

export default Activity;
