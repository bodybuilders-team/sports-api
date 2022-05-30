import LoginForm from "../components/LoginForm.js";
import {alertBoxWithError, storeUser} from "../js/utils.js";
import {API_BASE_URL} from "../js/config.js";

/**
 * Login page.
 *
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function LoginPage(state) {

    /**
     * Logins a user.
     * @param {Event} event form event
     */
    async function login(event) {
        event.preventDefault();
        const form = event.target;

        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;

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
            `${API_BASE_URL}/users/login`,
            {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password})
            }
        );

        const json = await res.json();

        if (res.ok) {
            storeUser(json);
            window.location.href = "#";
            return;
        }

        await alertBoxWithError(state, form, json.extraInfo);
    }

    return LoginForm(
        state,
        {onSubmit: login}
    );
}

export default LoginPage;