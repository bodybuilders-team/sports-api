import {changeLocation, createRef} from "../js/utils.js";
import {a, button, div, h5} from "../js/dom/domTags.js";

/**
 * LogoutDropdown component.
 *
 * @param {Object} state - application state
 * @param {Object} props - component properties
 *
 * @return Promise<HTMLElement>
 */
async function LogoutDropdown(state, props) {

    const dropdownRef = createRef();

    /**
     * Logout button click handler.
     *
     * @param {Event} event - click event
     */
    function signout(event) {
        event.preventDefault();
        localStorage.removeItem("user");

        changeLocation("/");
    }

    /**
     * Cancel button click handler.
     *
     * @param {Event} event - click event
     */
    async function cancelSignout(event) {
        event.preventDefault();

        const dropdown = await dropdownRef
        dropdown.classList.remove("show");
    }

    return div(
        {class: "dropdown"},
        a(
            {
                class: "nav-item nav-link dropdown-toggle",
                "data-bs-toggle": "dropdown",
                "aria-expanded": "false",
                href: "#"
            },
            "Logout"
        ),

        div({class: "dropdown-menu", ref: dropdownRef},
            div({class: "d-flex flex-column align-items-center"},
                h5({class: "mx-3 mb-3 font-weight-normal"}, "Are you sure you want to log out?"),
                div({class: "d-flex flex-column w-75"},
                    button({
                        type: "button",
                        onClick: signout,
                        class: "btn btn-danger mb-2"
                    }, "Log out"),
                    button({
                        type: "button",
                        onClick: cancelSignout,
                        class: "btn btn-primary"
                    }, "Cancel")
                ),
            )
        )
    );
}

export default LogoutDropdown;
