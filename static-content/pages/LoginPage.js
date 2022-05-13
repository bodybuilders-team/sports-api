import LoginForm from "../components/LoginForm.js";
import {br, div} from "../js/dom/domTags.js";

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

        // TODO: API login is not implemented yet
        const res = await fetch(
            "/api/login",
            {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: {email, password}
            }
        );
        const json = await res.json();

        if (res.ok)
            window.localStorage.setItem("token", json.token);

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

        window.location.href = "home";
    }

    return LoginForm(
        state,
        {onSubmit: login}
    );
}

export default LoginPage;