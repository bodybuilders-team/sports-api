import {br, button, div, form, h1, hr, input, label, option, select} from "../../js/dom/domTags.js";
import apiFetch from "../../js/apiFetch.js";
import OverflowInfinitePaginate from "../pagination/OverflowInfinitePaginate.js";
import {createRef} from "../../js/utils.js";

/**
 * @typedef PropActivitiesProps
 * @property {?number} sid - Search sport id
 * @property {?number} rid - Search route id
 * @property {?string} orderBy - Search orderBy {"ascending" || "descending"}
 * @property {?string} date - Search date
 */

/**
 * @callback OnSubmitCallback
 * @param {Event} event - event
 */

/**
 * SearchActivitiesForm component.
 *
 * @param state - application state
 *
 * @param {Object} props - component properties
 * @param {OnSubmitCallback} props.onSubmit - on Submit event callback
 * @param {?PropActivitiesProps=} props.activitiesProps - form activities props
 * @param {} props.sports - form sports
 * @param {} props.routes - form routes
 *
 * @return Promise<HTMLElement>
 */
async function SearchActivitiesForm(state, props) {
    const actProps = props.activitiesProps;

    const sportsFetchParams = new URLSearchParams()

    const sportsDropdownRef = createRef()
    const sportsResetRef = createRef()
    const sportsIdInputRef = createRef()

    let totalSportsCount = null
    let sportsSkip = 0

    const routesFetchParams = new URLSearchParams()

    const routesDropdownRef = createRef()
    const routesResetRef = createRef()
    const routeIdInputRef = createRef()

    let totalRoutesCount = null
    let routesSkip = 0

    async function onSportInputChange(event) {
        event.preventDefault();
        const sportName = event.target.value;
        if (sportName !== "")
            sportsFetchParams.set("name", sportName);
        else
            sportsFetchParams.delete("name");

        sportsSkip = 0
        totalSportsCount = null

        const sportsReset = await sportsResetRef.current

        await sportsReset()
    }

    async function onSelectedSportChange(event) {
        event.preventDefault();

        const dropdownBtn = await sportsDropdownRef.current
        const sportIdInput = await sportsIdInputRef.current

        dropdownBtn.textContent = event.target.textContent
        sportIdInput.value = event.target.dataset["id"];
    }

    async function onLoadMoreSports(numberSports) {
        if (totalSportsCount != null && sportsSkip + 1 >= totalSportsCount)
            return []

        sportsFetchParams.set("skip", sportsSkip)
        sportsFetchParams.set("limit", numberSports)

        const {
            sports,
            totalCount: newTotalCount,
        } = await apiFetch(`/sports?${sportsFetchParams.toString()}`)

        totalSportsCount = newTotalCount
        sportsSkip += numberSports

        return Promise.all(sports.map(async sport => {
                const btn = await button({class: "dropdown-item", "data-id": sport.id}, sport.name)
                btn.addEventListener("click", onSelectedSportChange)

                return btn
            }
        ))
    }

    async function onStartRouteLocationInputChange(event) {
        event.preventDefault();
        const startLocation = event.target.value;

        if (startLocation !== "")
            routesFetchParams.set("startLocation", startLocation);
        else
            routesFetchParams.delete("startLocation");

        routesSkip = 0
        totalRoutesCount = null

        const routesReset = await routesResetRef.current

        await routesReset()
    }

    async function onEndRouteLocationInputChange(event) {
        event.preventDefault();
        const endLocation = event.target.value;

        if (endLocation !== "")
            routesFetchParams.set("endLocation", endLocation);
        else
            routesFetchParams.delete("endLocation");

        routesSkip = 0
        totalRoutesCount = null

        const routesReset = await routesResetRef.current

        await routesReset()
    }

    async function onSelectedRouteChange(event) {
        event.preventDefault();

        const dropdownBtn = await routesDropdownRef.current
        const routeIdInput = await routeIdInputRef.current

        dropdownBtn.textContent = event.target.textContent

        routeIdInput.value = event.target.dataset["id"];
    }

    async function onLoadMoreRoutes(numberRoutes) {
        if (totalRoutesCount != null && routesSkip + 1 >= totalRoutesCount)
            return []

        sportsFetchParams.set("skip", routesSkip)
        sportsFetchParams.set("limit", numberRoutes)

        const {
            routes,
            totalCount,
        } = await apiFetch(`/routes?${routesFetchParams.toString()}`)

        totalRoutesCount = totalCount
        routesSkip += numberRoutes

        return Promise.all(routes.map(async route => {
                const btn = await button({
                    class: "dropdown-item",
                    "data-id": route.id
                }, route.startLocation + " - " + route.endLocation)
                btn.addEventListener("click", onSelectedRouteChange)

                return btn
            }
        ))
    }

    return div(
        {class: "card card-body w-50 center bg-light"},
        h1("Search for an Activity"),
        hr(),
        form(
            {onSubmit: props.onSubmit},
            div(
                label({for: "sid", class: "col-form-label"}, "Sport"),
                div({class: "dropdown"},
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
                        input({
                            type: "text",
                            class: "form-control",
                            onInput: onSportInputChange
                        }),
                        input({
                            type: "hidden",
                            id: "sid",
                            ref: sportsIdInputRef
                        }),
                        OverflowInfinitePaginate(state, {
                            onLoadMore: onLoadMoreSports,
                            resetRef: sportsResetRef,
                            initialNumChildren: 10,
                            numChildren: 5,
                            overflowHeight: "100px"
                        })
                    )
                ),
                label({for: "rid", class: "form-label"}, "Route"),
                div({class: "dropdown"},
                    button(
                        {
                            class: "btn btn-secondary dropdown-toggle w-100",
                            type: "button",
                            id: "dropdownMenuButton",
                            "data-bs-toggle": "dropdown",
                            "aria-expanded": "false",
                            ref: routesDropdownRef
                        },
                        "Select a route"
                    ),

                    div(
                        {class: "dropdown-menu w-100", "aria-labelledby": "dropdownMenuButton"},
                        div(
                            input({
                                type: "text", class: "form-control",
                                onInput: onStartRouteLocationInputChange
                            }),
                            input({
                                type: "text", class: "form-control",
                                onInput: onEndRouteLocationInputChange
                            }),
                        ),

                        input({
                            type: "hidden",
                            id: "rid",
                            ref: routeIdInputRef
                        }),
                        OverflowInfinitePaginate(state, {
                            onLoadMore: onLoadMoreRoutes,
                            resetRef: routesResetRef,
                            initialNumChildren: 10,
                            numChildren: 5,
                            overflowHeight: "100px"
                        })
                    )
                ),

                label({for: "orderBy", class: "form-label"}, "Order by"),
                select(
                    {id: "orderBy", class: "form-control", required: true},
                    option({
                        value: "",
                        selected: (actProps == null) ? "" : undefined
                    }, "Select a order to display the results"),
                    option({
                            value: "ascending",
                            selected: (actProps != null && actProps.orderBy === "ascending") ? "" : undefined
                        },
                        "Ascending"),
                    option({
                        value: "descending",
                        selected: (actProps != null && actProps.orderBy === "descending") ? "" : undefined
                    }, "Descending")
                ),

                label({for: "date", class: "form-label"}, "Date"),
                input({
                    type: "date", id: "date", class: "form-control",
                    value: (actProps != null && actProps.date != null) ? actProps.date : ""
                })
            ),
            br(),
            button({type: "submit", class: "btn btn-primary w-100"}, "Search")
        )
    )
}

export default SearchActivitiesForm;