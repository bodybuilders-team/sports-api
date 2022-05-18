import {div, h1} from "../../js/dom/domTags.js";

/**
 * The app error component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {string} props.name
 * @param {string} props.description
 * @param {?Object} props.extraInfo
 *
 * @returns Promise<HTMLElement>
 */
async function AppErrorPage(state, props) {

    return div(
        h1(`Error: ${props.name}`),
        h1(`Description: ${props.description}`),
        props.extraInfo != null
            ? h1(`Extra info: ${props.extraInfo}`)
            : undefined
    );
}


export default AppErrorPage;