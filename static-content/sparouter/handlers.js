/*
This example creates the students views using directly the DOM Api
But you can create the views in a different way, for example, for the student details you can:
    createElement("ul",
        createElement("li", "Name : " + student.name),
        createElement("li", "Number : " + student.number)
    )
or
    ul(
        li("Name : " + student.name),
        li("Number : " + student.name)
    )
Note: You have to use the DOM Api, but not directly
*/

const API_BASE_URL = "http://localhost:8888/api/"

function getHome(mainContent){
    h1("Home")
    mainContent.replaceChildren(h1)
}

function getUsers(mainContent){
    fetch(API_BASE_URL + "users")
        .then(res => res.json())
        .then(users => users.users)
        .then(users => {
            div(
                h1("Users"),
                users.forEach(s => {
                                p(a("Link Example to users/" + s.number))

                                a.href="#users/" + s.number
                                div.appendChild(p)
                            })
            )

            mainContent.replaceChildren(div)
        })
}

export const handlers = {
    getHome,
    getUsers
}

export default handlers