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
    async function login(event) {
        event.preventDefault();
        // const form = event.target;
        //
        // const email = form.querySelector("#email").value;
        // const password = form.querySelector("#password").value;
        //
        // // TODO: API login is not implemented yet
        // const res = await fetch(
        //     "/api/login",
        //     {
        //         method: "POST",
        //         headers: {'Content-Type': 'application/json'},
        //         body: {email, password}
        //     }
        // );
        // const json = await res.json();
        //
        // if (res.ok) {
        //     window.localStorage.setItem("token", json.token);
        //     window.location.href = "#home";
        //     return
        // }
        //
        // await alertBoxWithError(state, form, json);

        window.localStorage.setItem("token", "663f6889-e7b8-4eb4-8698-38d72de4a956");
        window.location.href = "#home";
    }

    return LoginForm(
        state,
        {onSubmit: login}
    );
}

export default LoginPage;