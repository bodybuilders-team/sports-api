/**
 *
 * @param state
 * @param props
 * @returns {Promise<void>}
 * @constructor
 */
import {div, h1, p} from "../../js/dom/domTags.js";

async function InvSearchParamsErrorPage(state, props) {
    return div(
        h1("Invalid search parameters"),
        p(props.error),
        (props.details != null) ?
            p(JSON.stringify(props.details))
            :
            null
    )
}

export default InvSearchParamsErrorPage;