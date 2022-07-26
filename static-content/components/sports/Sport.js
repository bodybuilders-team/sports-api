import {br, div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";
import EditSport from "./EditSport.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * @typedef ActivitiesData
 * @property {number} skip - activities skip
 * @property {number} limit - activities limit
 * @property {PropActivity[]} activities - activities
 * @property {number} totalCount - total number of activities
 */

/**
 * Sport component.
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - sport id
 * @param {string} props.name - sport name
 * @param {string} props.description - sport description
 * @param {number} props.uid - sport creator id
 * @param {ActivitiesData} props.activitiesData - activities data
 * @param {onUpdateCallback} props.onSportUpdated - callback to be called when sport is updated
 *
 * @return Promise<HTMLElement>
 */
async function Sport(state, props) {

    const {id, name, description, uid, activitiesData, onSportUpdated} = props;
    const storedUser = getStoredUser();

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Sport`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "sportName"}, "Name: ", name),
                h3({id: "sportDescription"}, "Description: ", description),
                br(),
                (storedUser != null && storedUser.uid === uid)
                    ? EditSport(state, {id, onSportUpdated})
                    : undefined,
                br(),
                (activitiesData.activities.length > 0)
                    ? div(
                        h3("Activities:"),
                        PaginatedCollection(state,
                            {
                                skip: activitiesData.skip,
                                limit: activitiesData.limit,
                                collectionComponent: Activities,
                                collectionName: "activities",
                                collection: activitiesData.activities,
                                totalCount: activitiesData.totalCount
                            }
                        )
                    )
                    : undefined
            )
        )
    );
}

export default Sport;
