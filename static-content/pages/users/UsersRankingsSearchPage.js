import {br, div, h1} from "../../js/dom/domTags.js";
import SearchUsersForm from "../../components/users/SearchUsersForm.js";

async function UsersRankingsSearchPage(state) {

    /**
     * Search for users form function.
     */
    function searchUsers(usersData) {
        const {sid, rid} = usersData;

        const searchParams = new URLSearchParams();
        searchParams.set("sid", sid);
        if (rid != null) searchParams.set("rid", rid);

        window.location.href = "#users?" + searchParams.toString();
    }

    return div(
        h1({class: "app-icon"}, "Users Rankings"),
        br(),
        SearchUsersForm(
            state, {
                onSubmit: searchUsers
            }
        )
    );
}

export default UsersRankingsSearchPage;