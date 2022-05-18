import RegisterForm from "../components/RegisterForm.js";
import {alertBoxWithError} from "../js/utils.js";

/**
 * Register page.
 *
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

        if (username.length < 3 || username.length > 60) {
            await alertBoxWithError(state, form, "Username must be between 3 and 60 characters long.");
            return;
        }

        if (email.length < 4 || email.length > 320) {
            await alertBoxWithError(state, form, "An email must be between 4 and 320 characters long.");
            return;
        }

        if (!email.match(/^[A-Za-z\d+_.-]+@(.+)$/)) {
            await alertBoxWithError(state, form, "Invalid email.");
            return;
        }

        if (!password.match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/)) {
            await alertBoxWithError(state, form,
                "A password must contain at least one letter and one number, and be at least 8 characters long.");
            return;
        }

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