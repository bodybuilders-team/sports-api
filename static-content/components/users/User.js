import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import {LogError} from "../../js/errorUtils.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";

/**
 * User details page.
 * @param state application state
 * @param props component properties
 * @returns user page
 */
async function User(state, props) {
    if (props == null)
        throw new LogError("User props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `User ${props.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body col justify-content-center"},
                h3("Username: ", props.name),
                h3("Email: ", props.email),
                (props.activitiesData.activities.length > 0) ?
                    div(
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
                    ) : undefined
            )
        )
    );
}

export default User;
