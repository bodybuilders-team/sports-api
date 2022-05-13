import RegisterForm from "../components/RegisterForm.js";
import {br, div} from "../js/dom/domTags.js";

/**
 * Register new user page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RegisterPage(state) {

    /**
     * Register a new user.
     * @param event form event
     */
    async function register(event) {
        event.preventDefault();
        const form = event.target;

        const username = form.querySelector("#username").value;
        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value; // TODO password

        const res = await fetch(
            "http://localhost:8888/api/users",
            {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({name: username, email})
            }
        );

        const json = await res.json();

        if (res.ok)
            window.location.href = "login";

        const alertBox = form.parentNode.querySelector("#alert_box");
        alertBox
            ? alertBox.innerHTML = json.extraInfo
            : await form.parentNode.appendChild(
                await div(
                    br(),
                    div(
                        {id: "alert_box", class: "alert alert-warning", role: "alert"},
                        json.extraInfo
                    )
                )
            );
    }

    return RegisterForm(
        state,
        {onSubmit: register}
    );
}

export default RegisterPage;