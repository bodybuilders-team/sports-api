import RegisterForm from "../components/RegisterForm.js";
import {alertBoxWithError} from "../js/utils.js";

/**
 * Register new user page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function RegisterPage(state) {

    /**
     * Register a new user.
     * @param {Event} event form event
     */
    async function register(event) {
        event.preventDefault();
        const form = event.target;

        const username = form.querySelector("#username").value;
        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;

        const res = await fetch(
            "http://localhost:8888/api/users",
            {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({name: username, email, password})
            }
        );

        const json = await res.json();

        if (res.ok)
            window.location.href = "#login";
        else
            await alertBoxWithError(state, form, json.extraInfo);
    }

    return RegisterForm(
        state,
        {onSubmit: register}
    );
}

export default RegisterPage;