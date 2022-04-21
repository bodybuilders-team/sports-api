import {div, h1, h3} from "../../js/dom/domTags.js";
import Activities from "../activities/Activities.js";
import {LogError} from "../../js/errorUtils.js";
import PaginatedCollection from "../pagination/PaginatedCollection.js";

/**
 * Sport details page.
 * @param state application state
 * @param props component properties
 * @returns sport page
 */
async function Sport(state, props) {
    if (props == null)
        throw new LogError("Sport props must not be null");

    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, `Sport ${props.id}`),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Name: ", props.name),
                h3("Description: ", props.description),
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

export default Sport;
