import {br, button, div, form, h1, hr, input, label} from "../js/dom/domTags.js";

/**
 * LoginForm component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onSubmit - on Submit event callback
 *
 * @return Promise<HTMLElement>
 */
async function LoginForm(state, props) {
    return div(
        {class: "card card-body w-50 center"},
        h1("Login"),
        hr(),
        form(
            {onSubmit: props.onSubmit},
            div(
                label({for: "email", class: "form-label"}, "Email"),
                input({
                    type: "email", id: "email",
                    class: "form-control",
                    placeholder: "Enter your email",
                    required: true
                }),

                label({for: "password", class: "form-label"}, "Password"),
                input({
                    type: "password", id: "password", pattern: "[0-9a-fA-F]{8,}",
                    title: "Enter a password with more than 8 hexadecimal digits",
                    class: "form-control",
                    placeholder: "Enter your password",
                    required: true
                }),
            ),
            br(),
            button({type: "submit", class: "btn btn-primary w-100 btn-lg"}, "Login")
        )
    );
}

export default LoginForm;
