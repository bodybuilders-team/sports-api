import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";

/**
 * User details page.

 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - user id
 * @param {string} props.name - username
 * @param {string} props.email - user email
 * @param {Object} props.activitiesData
 * @param {number} props.activitiesData.skip - activities skip
 * @param {number} props.activitiesData.limit - activities limit
 * @param {PropActivity[]} props.activitiesData.activities - activities
 * @param {number} props.activitiesData.totalCount - total number of activities
 *
 * @return Promise<HTMLElement>
 */
async function User(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `User ${props.id}`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body col justify-content-center "},
                h3("Username: ", props.name),
                h3("Email: ", props.email),
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

export default User;
