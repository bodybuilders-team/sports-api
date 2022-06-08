import {a, div, h5} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";

/**
 * UserCard component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {number} props.user.id - user id
 * @param {String} props.user.name - user name
 * @param {number} props.aid - activity id
 *
 * @returns {Promise<HTMLElement>}
 */
async function UsersRankingCard(state, props) {

    const {user, aid} = props;

    const {id, name} = user;

    const activity = await apiFetch(`/activities/${aid}`)

    return div(
        {class: "card user-card col-6 bg-light"},
        div(
            {class: "card-body d-flex justify-content-center"},
            a({href: `#users/${id}`},
                h5({class: "card-title"}, `${name} ${activity.duration}` )
            )
        )
    );
}

export default UsersRankingCard;
