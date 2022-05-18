import {div, h1, p} from "../../js/dom/domTags.js";

/**
 * Invalid search parameters error page.
 *
 * @param {Object} state - application state
 * @param {InvalidSearchParamsError} props - component properties
 *
 * @returns Promise<HTMLElement>
 */
async function InvalidSearchParamsErrorPage(state, props) {

    return div(
        h1("Invalid search parameters"),
        p(props.error),
        (props.details != null)
            ? p(JSON.stringify(props.details))
            : null
    );
}

export default InvalidSearchParamsErrorPage;