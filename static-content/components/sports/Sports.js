import {div} from "../../js/dom/domTags.js";

/**
 * @typedef PropSport
 * @property {number} id sport id
 * @property {string} name sport name
 */

/**
 * Sports component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {PropSport[]} props.sports - sports Ids
 *
 * @return Promise<HTMLElement>
 */
async function Sports(state, props) {

    return div(
        {class: "row justify-content-evenly"},
        ...props.sports.map(sport =>
            SportCard(state, sport)
        )
    );
}

export default Sports;