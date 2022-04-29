import {a, div, h5} from "../../js/dom/domTags.js";

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
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5({class: "card-title"}, a({href: `#sports/${sport.id}`}, sport.name))
                )
            )
        )
    );
}

export default Sports;