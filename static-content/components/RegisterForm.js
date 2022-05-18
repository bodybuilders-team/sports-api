import {br, button, div, form, h1, hr, input, label} from "../js/dom/domTags.js";

/**
 * RegisterForm component.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onSubmit - onSubmit event callback
 *
 * @return Promise<HTMLElement>
 */
async function RegisterForm(state, props) {

    const {onSubmit} = props;

    return div(
        {class: "card card-body w-50 center"},
        h1("Register"),
        hr(),
        form(
            {onSubmit: onSubmit},
            div(
                label({for: "username", class: "col-form-label"}, "Name"),
                input({
                    type: "text", id: "username", name: "username",
                    class: "form-control",
                    placeholder: "Enter your name", minlength: "3", maxlength: "60",
                    required: true
                }),

                label({for: "email", class: "form-label"}, "Email"),
                input({
                    type: "email", id: "email",
                    class: "form-control",
                    placeholder: "Enter your email", minlength: "4", maxlength: "320",
                    required: true
                }),

                label({for: "password", class: "form-label"}, "Password"),
                input({
                    type: "password", id: "password", pattern: "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                    title: "Enter a password with more than 8 hexadecimal digits",
                    class: "form-control",
                    placeholder: "Enter your password", minlength: "8",
                    required: true
                }),
            ),
            br(),
            button({type: "submit", class: "btn btn-primary w-100 btn-lg"}, "Register")
        )
    );
}

export default RegisterForm;
