import {createRef} from "../../js/utils.js";
import apiFetch from "../../js/apiFetch.js";
import {button, div, input, label} from "../../js/dom/domTags.js";
import OverflowInfinitePaginate from "../pagination/OverflowInfinitePaginate.js";

async function RoutesDropdown(state, props) {
    const {onChange} = props

    const routesFetchParams = new URLSearchParams()

    const routesDropdownRef = createRef()
    const routesResetRef = createRef()

    let totalRoutesCount = null
    let routesSkip = 0

    const generateNoRouteBtn = () =>
        button({
            class: "dropdown-item",
            "data-id": "",
            onClick: onSelectedRouteChange
        }, "No route")


    async function onStartRouteLocationInputChange(event) {
        event.preventDefault();
        const startLocation = event.target.value;

        if (startLocation !== "")
            routesFetchParams.set("startLocation", startLocation);
        else
            routesFetchParams.delete("startLocation");

        routesSkip = 0
        totalRoutesCount = null

        const routesReset = await routesResetRef

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

        const routesReset = await routesResetRef

        await routesReset()
    }

    async function onLoadMoreRoutes(numberRoutes) {
        if (totalRoutesCount != null && routesSkip + 1 >= totalRoutesCount)
            return []


        routesFetchParams.set("skip", routesSkip)
        routesFetchParams.set("limit", numberRoutes)

        const {
            routes,
            totalCount,
        } = await apiFetch(`/routes?${routesFetchParams.toString()}`)


        let routeBtns = routes.map(route =>
            button({
                class: "dropdown-item",
                "data-id": route.id,
                onClick: onSelectedRouteChange
            }, route.startLocation + " - " + route.endLocation)
        )

        if (routesSkip === 0)
            routeBtns.unshift(generateNoRouteBtn())

        totalRoutesCount = totalCount
        routesSkip += numberRoutes

        return routeBtns
    }

    async function onSelectedRouteChange(event) {
        event.preventDefault();

        const dropdownBtn = await routesDropdownRef
        dropdownBtn.textContent = event.target.textContent

        const id = event.target.dataset["id"]

        onChange(id)
    }

    return div({class: "dropdown"},
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
            div({class: "mx-3 mb-2"},
                label({for: "routeStartLocation"}, "Start location"),
                input({
                    type: "text", id: "routeStartLocation", class: "form-control routes-dropdown-input",
                    onInput: onStartRouteLocationInputChange
                }),
                label({for: "routeEndLocation"}, "End location"),
                input({
                    type: "text", id: "routeEndLocation", class: "form-control routes-dropdown-input",
                    onInput: onEndRouteLocationInputChange
                }),
            ),

            OverflowInfinitePaginate(state, {
                onLoadMore: onLoadMoreRoutes,
                resetRef: routesResetRef,
                initialNumChildren: 10,
                numChildren: 5,
                overflowHeight: "100px"
            })
        )
    )

}

export default RoutesDropdown;