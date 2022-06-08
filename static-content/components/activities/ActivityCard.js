import {a, div, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * ActivityCard component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.id - activity id
 * @param {string} props.date - activity date
 *
 * @returns {Promise<HTMLElement>}
 */
async function ActivityCard(state, props) {

    const {id, date, sid, rid} = props;

    const sport = await apiFetch(`/sports/${sid}`)
    const route = (rid != null) ? await apiFetch(`/routes/${rid}`) : null

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            h5({class: "card-title"}, a({href: `#activities/${id}`}, `${sport.name}  ${date} ${
                (route != null) ? (route.startLocation + '-'+route.endLocation) : ("")}`
            ))
        )
    );
}

export default ActivityCard;