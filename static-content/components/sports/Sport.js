import {br, div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";
import EditSport from "./EditSport.js";
import {getStoredUser} from "../../js/utils.js";

/**
 * Sport details page.
 *
 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - sport id
 * @param {string} props.name - sport name
 * @param {string} props.description - sport description
 * @param {Object} props.activitiesData
 * @param {number} props.activitiesData.skip - activities skip
 * @param {number} props.activitiesData.limit - activities limit
 * @param {PropActivity[]} props.activitiesData.activities - activities
 * @param {number} props.activitiesData.totalCount - total number of activities
 * @param {OnSubmitCallback} props.onUpdateSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function Sport(state, props) {
    const {onSportUpdated} = props

    const storedUser = getStoredUser();

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `Sport`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body"},
                h3({id: "sportName"}, "Name: ", props.name),
                h3({id: "sportDescription"}, "Description: ", props.description),
                br(),
                (storedUser != null && storedUser.uid === props.uid)
                    ? EditSport(state, {onSportUpdated})
                    : undefined,
                br(),
                (props.activitiesData.activities.length > 0)
                    ? div(
                        h3("Activities:"),
                        PaginatedCollection(state,
                            {
                                skip: props.activitiesData.skip,
                                limit: props.activitiesData.limit,
                                collectionComponent: Activities,
                                collectionName: "activities",
                                collection: props.activitiesData.activities,
                                totalCount: props.activitiesData.totalCount
                            }
                        )
                    )
                    : undefined
            )
        )
    );
}

export default Sport;
