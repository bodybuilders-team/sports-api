import {br, div} from "../js/dom/domTags.js";

/**
 * AlertBox component.

 * @param {Object} state - global state
 *
 * @param {Object} props - component properties
 * @param {string} props.alertLevel - alert box level (e.g. "success", "info", "warning", "danger")
 * @param {string} props.alertMessage - alert box message
 *
 * @return Promise<HTMLElement>
 */
async function AlertBox(state, props) {

    const {alertLevel, alertMessage} = props;

    return div(
        br(),
        div(
            {id: "alert_box", class: `alert alert-${alertLevel}`, role: "alert"},
            alertMessage
        )
    );
}

export default AlertBox;
