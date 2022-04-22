/**
 * Creates a router.
 * @returns created router
 */
import {LogError} from "../js/errorUtils.js";

export function Router() {
    function router(state, props) {
        return router.handle(state, props)
    }

    router.handle = function (state, props) {
        if (state.currentPath == null)
            state.currentPath = state.path;

        const handlerData = this.getHandler(state.currentPath);

        if (handlerData == null) {
            if (this.defaultHandler != null)
                return this.defaultHandler(state, props);

            throw new LogError(`No handler for path ${state.currentPath}`);
        }

        if (state.params == null)
            state.params = {};

        for (const param in handlerData.params)
            state.params[param] = handlerData.params[param];

        state.currentPath = handlerData.newPath;

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

            const {isMatch, newPath, params} = matchHandlerPath(handlerPath, path);

            if (!isMatch)
                continue

            return {
                handler: handlerData.handler,
                handlerPath,
                newPath,
                params
            };

        }

        return undefined;
    }

    /**
     * Matches a handler path with a given path and fills params if they exist.
     * @param handlerPath handler path
     * @param path path to match
     *
     */
    function matchHandlerPath(handlerPath, path) {
        let lIdx = -1;
        let rIdx = path.indexOf('/');

        let lHandlerIdx = -1;
        let rHandlerIdx = handlerPath.indexOf('/');

        const params = {}
        let newPath = path

        while (lHandlerIdx < rHandlerIdx) {
            const currPath = path.substring(lIdx + 1, rIdx);

            const currHandlerPath = handlerPath.substring(lHandlerIdx + 1, rHandlerIdx);

            if (currHandlerPath.startsWith(':') && currPath !== "") {
                const paramName = currHandlerPath.substring(1);
                params[paramName] = currPath;
            } else if (currHandlerPath !== currPath)
                return {isMatch: false};

            lIdx = rIdx;
            lHandlerIdx = rHandlerIdx;

            rIdx = path.indexOf('/', rIdx + 1);
            rHandlerIdx = handlerPath.indexOf('/', rHandlerIdx + 1);

            if (rIdx === -1)
                rIdx = path.length;

            if (rHandlerIdx === -1)
                rHandlerIdx = handlerPath.length;
        }

        newPath = path.substring(lIdx);

        return {isMatch: true, newPath, params};
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
