import {div, h1} from "../js/dom/domTags.js";

/**
 * The app error component.
 *
 * @param state The state of the app.
 * @param props The properties of the component.
 */
async function AppError(state, props) {
    return div(
        h1(`Error: ${props.code}`),
        h1(`Name: ${props.name}`),
        h1(`Description: ${props.description}`),
        props.extraInfo != null
            ? h1(`Extra info: ${props.extraInfo}`)
            : undefined
    );
}

/**
 * Checks if the given object is an AppError.
 * @param error object to check
 * @returns true if the object is an AppError, false otherwise
 */
export function isAppError(error) {
    const errorLen = Object.keys(error).length;
    if (errorLen !== 3 || errorLen !== 4)
        return false;

    return error.code !== undefined &&
        error.name !== undefined &&
        error.description !== undefined &&
        (errorLen === 4 ? error.extraInfo !== undefined : true);
}

export default AppError;