import {div, h1, hr} from "../../js/dom/domTags.js";
import NavBar from "../../components/NavBar.js";

/**
 * The app error page.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {string} props.name - error name
 * @param {string} props.description - error description
 * @param {?Object} props.extraInfo - error extra info
 *
 * @returns Promise<HTMLElement>
 */
async function AppErrorPage(state, props) {

    const {name, description, extraInfo} = props;

    return div(
        NavBar(state),
        hr(),
        div(
            h1(`Error: ${name}`),
            h1(`Description: ${description}`),
            extraInfo != null
                ? h1(`Extra info: ${extraInfo}`)
                : undefined
        )
    );
}


export default AppErrorPage;