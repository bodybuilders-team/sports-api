import LoginForm from "../components/LoginForm.js";
import {alertBoxWithError} from "../js/utils.js";

/**
 * Login page.
 * @param {Object} state - application state
 *
 * @returns Promise<HTMLElement>
 */
async function LoginPage(state) {

    /**
     * Logins a user.
     * @param event form event
     */
    async function login(event) {
        event.preventDefault();
        const form = event.target;

        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;

        const res = await fetch(
            "http://localhost:8888/api/users/login",
            {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password})
            }
        );

        const json = await res.json();

        if (res.ok) {
            window.localStorage.setItem("user", JSON.stringify(json));
            window.location.href = "#";
            return
        }

        await alertBoxWithError(state, form, json.extraInfo);
    }

    return LoginForm(
        state,
        {onSubmit: login}
    );
}

export default LoginPage;