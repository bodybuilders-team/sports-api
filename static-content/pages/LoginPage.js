import LoginForm from "../components/LoginForm.js";

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
    function login(event) {
        event.preventDefault();
        const form = event.target;

        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;

        // TODO: API login is not implemented yet
        fetch("/api/login", {method: "POST"});

        window.location.href = "home";
    }

    return LoginForm(
        state,
        {onSubmit: login}
    );
}

export default LoginPage;