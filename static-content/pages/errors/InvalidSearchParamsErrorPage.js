import {div, h1, hr, p} from "../../js/dom/domTags.js";
import NavBar from "../../components/NavBar.js";

/**
 * Invalid search parameters error page.
 *
 * @param {Object} state - application state
 *
 * @param {InvalidSearchParamsError} props - component properties
 *
 * @returns Promise<HTMLElement>
 */
async function InvalidSearchParamsErrorPage(state, props) {

    const {error, details} = props;

    return div(
        NavBar(state),
        hr(),
        div(
            h1("Invalid search parameters"),
            div(
                {id: "alert_box", class: `alert alert-danger`, role: "alert"},
                p(error),
                (details != null)
                    ? p(JSON.stringify(details))
                    : null
            )
        )
    );
}

export default InvalidSearchParamsErrorPage;