/**
 * Creates a router.
 * @returns created router
 */
export function Router() {
    function router(state) {
        return router.handle(state)
    }

    router.handle = function (state) {
        const handlerData = this.getHandler(state.currentPath);
        if (handlerData === undefined) {
            const handler = this.defaultHandler(state);

            if (handler === undefined)
                throw new Error(`No handler for path ${state.currentPath}`);
            return handler;
        }

        const handlerPath = handlerData.path;

        for (const param in handlerData.params)
            state.params[param] = handlerData.params[param];

        state.currentPath = state.currentPath.substring(handlerPath.length);

        return handlerData.handler(state);
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
            const params = {};

            const isMatch = matchHandlerPath(handlerPath, path, params);
            if (isMatch)
                return {
                    handler: handlerData.handler,
                    path: handlerPath,
                    params: params
                };

        }

        return undefined;
    }

    function matchHandlerPath(handlerPath, path, params) {
        let lIdx = -1;
        let rIdx = path.indexOf('/');

        let lHandlerIdx = -1;
        let rHandlerIdx = handlerPath.indexOf('/');

        while (lHandlerIdx < rHandlerIdx) {
            // /users/10/edit/20/dada
            // /users/:id/edit/:groupId
            // /
            const currPath = path.substring(lIdx + 1, rIdx);
            const currHandlerPath = handlerPath.substring(lHandlerIdx + 1, rHandlerIdx);

            if (currHandlerPath.startsWith(':')) {
                const paramName = currHandlerPath.substring(1);
                params[paramName] = currPath;
            } else if (currHandlerPath !== currPath)
                return false;

            lIdx = rIdx;
            lHandlerIdx = rHandlerIdx;

            rIdx = path.indexOf('/', rIdx + 1);
            rHandlerIdx = handlerPath.indexOf('/', rHandlerIdx + 1);
            if (rIdx === -1)
                rIdx = path.length;
            if (rHandlerIdx === -1)
                rHandlerIdx = handlerPath.length;
        }

        return true;
    }


    /**
     * Adds a new handler.
     * @param path handler path
     * @param handler new handler
     */
    router.addHandler = function (path, handler) {
        if (typeof path !== 'string')
            throw new Error(`Path must be a string`);

        if (typeof handler !== 'function')
            throw new Error(`Handler for path ${path} is not a function`);

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
