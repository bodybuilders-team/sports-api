import {a, br, div, h1, h3, h5} from "../../js/dom/domTags.js";
import EditActivity from "./EditActivity.js";
import DeleteActivity from "./DeleteActivity.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * @typedef PropRoute
 * @property {string} id activity route id
 * @property {string} startLocation activity route start location
 * @property {string} endLocation activity route end location
 */

/**
 * @typedef PropSport
 * @property {number} id - activity sport id
 * @property {string} name - activity sport name
 */

/**
 * Activity details component.
 *
 * @param state - application state
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {string} props.date - activity date
 * @param {string} props.duration - activity duration
 * @param {PropSport} props.sport - activity sport
 * @param {?PropRoute=} props.route - activity route
 * @param {Function} props.onActivityUpdated - callback to update activity
 * @param {Function} props.onActivityDeleted - callback to delete activity
 *
 * @return Promise<HTMLElement>
 */
async function Activity(state, props) {

    const {id, date, duration, user, sport, route, onActivityUpdated, onActivityDeleted} = props;
    const storedUser = getStoredUser();

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Activity`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "activitySport"}, "Sport: ", a({href: `#sports/${sport.id}`}, sport.name)),
                h3({id: "activityDate"}, "Date: ", date),
                h3({id: "activityDuration"}, "Duration: ", duration),
                h3({id: "activityUser"}, "User: ", a({href: `#users/${user.id}`}, user.name)),
                route != null
                    ? h5({id: "activityRoute"}, "Route: ", a({href: `#routes/${route.id}`}, route.startLocation + " - " + route.endLocation))
                    : undefined,
                br(),
                (storedUser != null && storedUser.uid === user.id)
                    ? div(
                        EditActivity(state, {id, onActivityUpdated}),
                        br(),
                        DeleteActivity(state, {id, onActivityDeleted})
                    )
                    : undefined
            )
        )
    );
}

export default Activity;
