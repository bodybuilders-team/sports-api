import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";

/**
 * User component.

 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {string} props.name - username
 * @param {string} props.email - user email
 * @param {ActivitiesData} props.activitiesData - activities data
 *
 * @return Promise<HTMLElement>
 */
async function User(state, props) {

    const {name, email, activitiesData} = props;

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app-icon"}, `User`),
        div(
            {class: "card user-card col-6 bg-light"},
            div(
                {class: "card-body col justify-content-center "},
                h3("Username: ", name),
                h3("Email: ", email),
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

export default User;
