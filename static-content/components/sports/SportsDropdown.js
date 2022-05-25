import {createRef} from "../../js/utils.js";
import apiFetch from "../../js/apiFetch.js";
import {button, div, input, label} from "../../js/dom/domTags.js";
import OverflowInfinitePaginate from "../pagination/OverflowInfinitePaginate.js";

/**
 * SportsDropdown component, used to display a dropdown of searchable sports.
 *
 * @param {Object} state - application state
 *
 * @param {Object} props - component properties
 * @param {OnChangeCallback} props.onChange - callback to be called when the selected sport changes
 *
 * @return Promise<HTMLElement>
 */
async function SportsDropdown(state, props) {
    const {onChange} = props;

    const sportsFetchParams = new URLSearchParams();

    const sportsDropdownRef = createRef();
    const sportsResetRef = createRef();

    let totalSportsCount = null;
    let sportsSkip = 0;


    async function onSportInputChange(event) {
        event.preventDefault();
        const sportName = event.target.value;

        if (sportName !== "")
            sportsFetchParams.set("name", sportName);
        else
            sportsFetchParams.delete("name");

        sportsSkip = 0;
        totalSportsCount = null;

        const sportsReset = await sportsResetRef;

        await sportsReset();
    }

    async function onLoadMoreSports(numberSports) {
        if (totalSportsCount != null && sportsSkip + 1 >= totalSportsCount)
            return [];

        sportsFetchParams.set("skip", sportsSkip);
        sportsFetchParams.set("limit", numberSports);

        const {
            sports,
            totalCount: newTotalCount,
        } = await apiFetch(`/sports?${sportsFetchParams.toString()}`);

        totalSportsCount = newTotalCount;
        sportsSkip += numberSports;

        return sports.map(async sport =>
            button({
                class: "dropdown-item", "data-id": sport.id,
                onClick: onSelectedSportChange
            }, sport.name)
        );
    }

    async function onSelectedSportChange(event) {
        event.preventDefault();

        const dropdownBtn = await sportsDropdownRef;
        dropdownBtn.textContent = event.target.textContent;

        const sportId = event.target.dataset["id"];

        onChange(sportId);
    }


    return div({class: "dropdown"},
        button(
            {
                class: "btn btn-secondary dropdown-toggle w-100",
                type: "button",
                id: "dropdownMenuButton",
                "data-bs-toggle": "dropdown",
                "aria-expanded": "false",
                ref: sportsDropdownRef
            },
            "Select a sport"
        ),
        div(
            {class: "dropdown-menu w-100", "aria-labelledby": "dropdownMenuButton"},
            div({class: "mx-2 mb-2"},
                label({for: "sportName"}, "Sport name"),
                input({
                    type: "text",
                    id: "sportName",
                    class: "form-control",
                    onInput: onSportInputChange
                }),
            ),
            OverflowInfinitePaginate(state, {
                onLoadMore: onLoadMoreSports,
                resetRef: sportsResetRef,
                initialNumChildren: 10,
                numChildren: 5,
                overflowHeight: "100px"
            })
        )
    );
}

export default SportsDropdown;