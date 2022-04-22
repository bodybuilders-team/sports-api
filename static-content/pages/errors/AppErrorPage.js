import {div, h1} from "../../js/dom/domTags.js";

/**
 * The app error component.
 *
 * @param state The state of the app.
 * @param props The properties of the component.
 */
async function AppErrorPage(state, props) {
    return div(
        h1(`Error: ${props.code}`),
        h1(`Name: ${props.name}`),
        h1(`Description: ${props.description}`),
        props.extraInfo != null
            ? h1(`Extra info: ${props.extraInfo}`)
            : undefined
    );
}


export default AppErrorPage;