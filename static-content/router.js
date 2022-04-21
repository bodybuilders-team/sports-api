/**
 * Creates a router.
 * @returns created router
 */
import {LogError} from "./js/errorUtils.js";

export function Router() {
    function router(state, props) {
        return router.handle(state, props)
    }

    router.handle = function (state, props) {
        const handlerData = this.getHandler(state.currentPath);
        if (handlerData === undefined) {
            const handler = this.defaultHandler(state, props);

            if (handler === undefined)
                throw new LogError(`No handler for path ${state.currentPath}`);
            return handler;
        }

        const handlerPath = handlerData.path;

        for (const param in handlerData.params)
            state.params[param] = handlerData.params[param];

        for (const queryParam in handlerData.query)
            state.query[queryParam] = handlerData.query[queryParam];

        state.currentPath = state.currentPath.substring(handlerPath.length)

        return handlerData.handler(state, props);
    }

    /**
     * Gets the handler associated with the given path.
     * @param path handler path
     * @returns object representing the handler
     */
    router.getHandler = function (path) {
        if (path === "")
            path = "/";

        for (const handlerData of this.handlers) {
            const handlerPath = handlerData.path;

            const {isMatch, params, query} = matchHandlerPath(handlerPath, path);
            if (isMatch)
                return {
                    handler: handlerData.handler,
                    path: handlerPath,
                    params,
                    query
                };

        }

        return undefined;
    }

    function matchHandlerPath(handlerPath, path) {
        let lIdx = -1;
        let rIdx = path.indexOf('/');

        let lHandlerIdx = -1;
        let rHandlerIdx = handlerPath.indexOf('/');

        let params = {}
        let query = {}

        while (lHandlerIdx < rHandlerIdx) {
            // /users/10/edit/20/dada
            // /users/:id/edit/:groupId
            // /
            const currPath = path.substring(lIdx + 1, rIdx);
            const currPathQueryIdx = currPath.indexOf('?');

            const currPathWithoutQuery = currPathQueryIdx === -1 ? currPath : currPath.substring(0, currPathQueryIdx);
            if (currPathQueryIdx !== -1) {
                const queryStr = currPath.substring(currPathQueryIdx + 1)
                const queryParams = new URLSearchParams(queryStr);

                queryParams.forEach((value, key) => {
                    query[key] = value;
                })
            }

            const currHandlerPath = handlerPath.substring(lHandlerIdx + 1, rHandlerIdx);

            if (currHandlerPath.startsWith(':')) {
                const paramName = currHandlerPath.substring(1);
                params[paramName] = currPathWithoutQuery;
            } else if (currHandlerPath !== currPathWithoutQuery)
                return {isMatch: false};

            lIdx = rIdx;
            lHandlerIdx = rHandlerIdx;

            rIdx = path.indexOf('/', rIdx + 1);
            rHandlerIdx = handlerPath.indexOf('/', rHandlerIdx + 1);

            if (rIdx === -1)
                rIdx = path.length;

            if (rHandlerIdx === -1)
                rHandlerIdx = handlerPath.length;

            if (currPathQueryIdx !== -1 && lHandlerIdx < rHandlerIdx)
                return {isMatch: false};

        }

        return {isMatch: true, params, query};
    }


    /**
     * Adds a new handler.
     * @param path handler path
     * @param handler new handler
     */
    router.addHandler = function (path, handler) {
        if (typeof path !== 'string')
            throw new LogError(`Path must be a string`);

        if (typeof handler !== 'function')
            throw new LogError(`Handler for path ${path} is not a function`);

        this.handlers.push({path: path, handler: handler});
    }

    /**
     * Sets a default handler.
     * @param handler new default handler
     */
    router.addDefaultHandler = function (handler) {
        this.defaultHandler = handler;
    }

    router.handlers = [];

    return router;
}
