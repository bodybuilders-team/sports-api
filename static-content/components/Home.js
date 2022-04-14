import {a, br, div, h1, img, p, strong} from "../js/dom/domTags.js";

/**
 * Home page.
 * @param state application state
 * @returns home page
 */
async function Home(state) {
    return div(
        h1("Home"),
        p("Add description..."),
        strong(
            "Authors: ",
            a({href: "https://github.com/Andre-J3sus"}, "André Jesus"), ", ",
            a({href: "https://github.com/devandrepascoa"}, "André Páscoa"), " and ",
            a({href: "https://github.com/Nyckoka"}, "Nyckollas Brandão")
        ),
        p(
            "Project GitHub repository ",
            a({href: "https://github.com/isel-leic-ls/2122-2-LEIC41D-G03"}, "here")
        ),
        p("Professors: Engs. Filipe Freitas, Luís Falcão and Daniel Dias"),
        p("Bachelor in Computer Science and Computer Engineering"),
        p("Software Laboratory - LEIC41D - Group 03"),
        p("Summer Semester of 2021/2022"),
        br(),
        img({src: "/public/isel-logo.png", class: "rounded mx-auto d-block", alt: "ISEL Logo"})
    );
}

export default Home;